package dev.kord.x.commands.model.processor

import dev.kord.x.commands.internal.cast
import dev.kord.x.commands.model.command.Command
import dev.kord.x.commands.model.command.CommandEvent
import dev.kord.x.commands.model.context.CommonContext
import dev.kord.x.commands.model.eventFilter.EventFilter
import dev.kord.x.commands.model.module.Module
import dev.kord.x.commands.model.module.ModuleModifier
import dev.kord.x.commands.model.module.forEachModule
import dev.kord.x.commands.model.precondition.Precondition
import dev.kord.x.commands.model.prefix.Prefix
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging
import org.koin.core.Koin
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger { }

@Suppress("UndocumentedPublicProperty", "UndocumentedPublicClass")
data class CommandProcessorData(
        val filters: Map<ProcessorContext<*, *, *>, List<EventFilter<*>>>,
        val preconditions: Map<ProcessorContext<*, *, *>, List<Precondition<out CommandEvent>>>,
        var environment: BuildEnvironment,
        val prefix: Prefix,
        val handlers: Map<ProcessorContext<*, *, *>, EventHandler<*>>,
        var modifiers: List<ModuleModifier>,
        val koin: Koin,
        val dispatcher: CoroutineDispatcher = Dispatchers.Default
)

/**
 * A CommandProcessor contains all the information needed for parsing events and invoking commands,
 * and acts as a central dispatcher that relays events from their [EventSource] to their specific [EventHandler].
 */
class CommandProcessor(private val data: CommandProcessorData) : CoroutineScope {

    /**
     * Lock used for [rebuilding][rebuild] internal state.
     */
    private val editMutex = Mutex()

    override val coroutineContext: CoroutineContext = data.dispatcher + Job()

    /**
     * All filters in this processor grouped per context.
     */
    val filters: Map<ProcessorContext<*, *, *>, List<EventFilter<*>>> get() = data.filters

    /**
     * All preconditions in this processor grouped per context.
     */
    val preconditions: Map<ProcessorContext<*, *, *>, List<Precondition<out CommandEvent>>> get() = data.preconditions

    /**
     * All commands in this processor.
     */
    val commands: Map<String, Command<out CommandEvent>> get() = data.environment.commands

    /**
     * The collection of prefix suppliers used to get the prefix for a certain [ProcessorContext].
     */
    val prefix: Prefix get() = data.prefix

    /**
     * the [Koin] instance to be shared among the [CommandEvents][CommandEvent].
     */
    val koin: Koin get() = data.koin

    /**
     * Gets all preconditions for the [context], including the [CommonContext] preconditions.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : CommandEvent> getPreconditions(context: ProcessorContext<*, *, T>): List<Precondition<T>> {
        val commonPreconditions = data.preconditions[CommonContext].orEmpty()
        if (context == CommonContext) return commonPreconditions as List<Precondition<T>>

        val contextPreconditions = data.preconditions[context].orEmpty()
        return (commonPreconditions + contextPreconditions) as List<Precondition<T>>
    }

    /**
     * Gets a command by its [name], returns null when not found.
     */
    fun getCommand(name: String): Command<*>? = commands[name]

    /**
     * Gets a command by its [name] with the given [context], returns null when no command with the [name]
     * was found or its [Command.context] was different from the passed [context].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : CommandEvent> getCommand(context: ProcessorContext<*, *, T>, name: String): Command<T>? {
        val command = commands[name] ?: return null
        return when (command.context) {
            CommonContext -> command
            context -> command
            else -> null
        } as? Command<T>?
    }

    /**
     * Gets all event filters for the [context], including the [CommonContext] filters.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getFilters(context: ProcessorContext<T, *, *>): List<EventFilter<T>> = when (context) {
        is CommonContext -> (filters[CommonContext].orEmpty()) as List<EventFilter<T>>
        else -> (filters[context].orEmpty() + getFilters(CommonContext)) as List<EventFilter<T>>
    }

    /**
     * Gets the event handler for the [context], returns null if not present.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getEventHandler(context: ProcessorContext<T, *, *>): EventHandler<T>? {
        return data.handlers[context] as EventHandler<T>?
    }

    /**
     * Change mutable state under lock.
     */
    private suspend inline fun edit(modify: () -> Unit) {
        editMutex.withLock { modify() }
    }

    /**
     * Starts listening to the [source] launched in [CommandProcessorData.dispatcher].
     */
    fun <S> addSource(source: EventSource<S>): Job {
        return source.events.onEach {
            launch(data.dispatcher) {
                try {
                    handle(it, source.context.cast())
                } catch (exception: Exception) {
                    logger.catching(exception)
                }
            }
        }.catch { logger.catching(it) }.launchIn(this)
    }

    /**
     * Handles an [event] for a certain [context].
     *
     * @throws IllegalStateException when no [EventHandler] for the [context] was registered.
     */
    suspend fun <S> handle(
            event: S,
            context: ProcessorContext<S, *, *>
    ) {
        val handler = getEventHandler(context) ?: error("no handler registered for context ${context.javaClass.name}")
        with(handler) { onEvent(event) }
    }

    /**
     * Adds the [modifier] to the current modules and commands, rebuilding the processor in the process.
     *
     * > This is potentially a **very** expensive operation, as every command and module needs to be rebuild.
     * If you need to temporarily disable/enable an entity
     * consider doing so with an [EventFilter] or [Precondition] instead.
     */
    suspend operator fun plusAssign(modifier: ModuleModifier) = edit {
        data.modifiers += modifier
        rebuild()
    }

    /**
     * Adds the [modifiers] to the current modules and commands, rebuilding the processor in the process.
     *
     * > This is potentially a **very** expensive operation, as every command and module needs to be rebuild.
     * If you need to temporarily disable/enable an entity
     * consider doing so with an [EventFilter] or [Precondition] instead.
     */
    suspend fun addModules(vararg modifiers: ModuleModifier) = edit {
        data.modifiers += modifiers
        rebuild()
    }

    private suspend fun rebuild() {
        val container = ModuleContainer()
        data.modifiers.forEach { it.apply(container) }
        container.applyForEach()

        val environment = BuildEnvironment(koin)
        container.modules.values.forEach { it.build(environment) }

        data.environment = environment
    }

    /**
     * Removes the command from the current modules and commands, rebuilding the processor in the process.
     *
     * > This is potentially a **very** expensive operation, as every command and module needs to be rebuild.
     * If you need to temporarily disable/enable an entity
     * consider doing so with an [EventFilter] or [Precondition] instead.
     */
    suspend operator fun minusAssign(command: Command<*>) = edit {
        val name = command.name
        data.modifiers += forEachModule { commands.remove(name) }
        rebuild()
    }

    /**
     * Removes the module from the current modules and commands, rebuilding the processor in the process.
     *
     * > This is potentially a **very** expensive operation, as every command and module needs to be rebuild.
     * If you need to temporarily disable/enable an entity
     * consider doing so with an [EventFilter] or [Precondition] instead.
     */
    suspend operator fun minusAssign(module: Module) = edit {
        val name = module.name
        data.modifiers += object : ModuleModifier {
            override suspend fun apply(container: ModuleContainer) {
                container.remove(name)
            }
        }
        rebuild()
    }

}

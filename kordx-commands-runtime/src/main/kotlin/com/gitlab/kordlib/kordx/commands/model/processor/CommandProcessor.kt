package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.internal.cast
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter
import com.gitlab.kordlib.kordx.commands.model.module.Module
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.module.moduleModifier
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.model.prefix.Prefix
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging
import org.koin.core.Koin
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger { }

class CommandProcessor(
        val filters: Map<ProcessorContext<*, *, *>, List<EventFilter<*>>>,
        val preconditions: Map<ProcessorContext<*, *, *>, List<Precondition<out CommandEvent>>>,
        commands: Map<String, Command<out CommandEvent>>,
        val prefix: Prefix,
        private val handlers: Map<ProcessorContext<*, *, *>, EventHandler<*>>,
        private var modifiers: List<ModuleModifier>,
        val koin: Koin,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {
    private val editMutex = Mutex()
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    private var _commands: Map<String, Command<out CommandEvent>> = commands
    val commands: Map<String, Command<out CommandEvent>> get() = _commands

    @Suppress("UNCHECKED_CAST")
    fun <T : CommandEvent> getPreconditions(context: ProcessorContext<*, *, T>): List<Precondition<T>> {
        return preconditions[context].orEmpty() as List<Precondition<T>>
    }

    fun getCommand(name: String): Command<*>? = commands[name]

    fun <T : CommandEvent> getCommand(context: ProcessorContext<*, *, T>, name: String): Command<T>? {
        val command = commands[name] ?: return null
        if (command.context != context) return null

        @Suppress("UNCHECKED_CAST")
        return command as Command<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getFilters(context: ProcessorContext<T, *, *>): List<EventFilter<T>> {
        return when (context) {
            is CommonContext -> (filters[CommonContext].orEmpty()) as List<EventFilter<T>>
            else -> (filters[context].orEmpty() + getFilters(CommonContext)) as List<EventFilter<T>>
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEventHandler(context: ProcessorContext<T, *, *>): EventHandler<T>? {
        return handlers[context] as EventHandler<T>?
    }

    private suspend inline fun edit(modify: () -> Unit) {
        editMutex.withLock { modify() }
    }

    fun <S> addSource(source: EventSource<S>): Job {
        return source.events.onEach {
            try {
                handle(it, source.context.cast())
            } catch (exception: Exception) {
                logger.catching(exception)
            }
        }.catch { logger.catching(it) }.launchIn(this)
    }

    suspend fun <S> handle(
            event: S,
            context: ProcessorContext<S, *, *>
    ) {
        val handler = getEventHandler(context) ?: error("no handler registered for context ${context.javaClass.name}")
        with(handler) { onEvent(event) }
    }

    suspend operator fun plusAssign(modifier: ModuleModifier) = edit {
        modifiers += modifier
        rebuild()
    }

    private suspend fun rebuild() {
        val container = ModuleContainer()
        modifiers.forEach { it.apply(container) }
        container.applyForEach()

        val modules = _commands.values.firstOrNull()?.modules as? MutableMap<String, Module> ?: mutableMapOf()
        container.modules.values.forEach { it.build(modules, koin) }

        val map: Map<String, Command<out CommandEvent>> = modules.values.map { it.commands }.fold(emptyMap()) { acc, map ->
            map.keys.forEach { require(it !in acc) { "command $it is already registered in ${acc[it]!!.module.name}" } }
            acc + map
        }
        map.keys.forEach { require(it !in _commands) { "command $it is already registered ${_commands[it]!!.module.name}" } }
        _commands = map
    }

    suspend operator fun minusAssign(command: Command<*>) = edit {
        val name = command.name
        modifiers += moduleModifier { commands.remove(name) }
        rebuild()
    }

    suspend operator fun minusAssign(module: Module) = edit {
        val name = module.name
        modifiers += object : ModuleModifier {
            override suspend fun apply(container: ModuleContainer) {
                container.remove(name)
            }
        }
        rebuild()
    }

}
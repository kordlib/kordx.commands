package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.*
import com.gitlab.kordlib.kordx.commands.internal.cast
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
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger { }

class Pipe(
        val filters: Map<PipeContext<*, *, *>, List<EventFilter<*>>>,
        val preconditions: Map<PipeContext<*, *, *>, List<Precondition<out CommandContext>>>,
        commands: Map<String, Command<out CommandContext>>,
        val prefix: Prefix,
        private val handlers: Map<PipeContext<*, *, *>, EventHandler<*>>,
        private var modifiers: List<ModuleModifier>,
        val koin: Koin,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {
    private val editMutex = Mutex()
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    private var _commands: Map<String, Command<out CommandContext>> = commands
    val commands: Map<String, Command<out CommandContext>> get() = _commands

    @Suppress("UNCHECKED_CAST")
    fun <T : CommandContext> getPreconditions(context: PipeContext<*, *, T>): List<Precondition<T>> {
        return preconditions[context].orEmpty() as List<Precondition<T>>
    }

    fun <T : CommandContext> getCommand(context: PipeContext<*, *, T>, name: String): Command<T>? {
        val command = commands[name] ?: return null
        if (command.context != context) return null

        @Suppress("UNCHECKED_CAST")
        return command as Command<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getFilters(context: PipeContext<T, *, *>): List<EventFilter<T>> {
        return when (context) {
            is CommonContext -> (filters[CommonContext].orEmpty()) as List<EventFilter<T>>
            else -> (filters[context].orEmpty() + getFilters(CommonContext)) as List<EventFilter<T>>
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEventHandler(context: PipeContext<T, *, *>): EventHandler<T> {
        return handlers[context] as EventHandler<T>
    }

    private suspend inline fun edit(modify: () -> Unit) {
        editMutex.withLock { modify() }
    }

    fun <S> add(source: EventSource<S>): Job {
        return source.events.onEach {
            handle(it, source.context.cast())
        }.catch { logger.catching(it) }.launchIn(this)
    }

    suspend fun <S> handle(
            event: S,
            context: PipeContext<S, *, *>
    ) = with(getEventHandler(context)) { onEvent(event) }

    suspend operator fun plusAssign(modifier: ModuleModifier) = edit {
        modifiers += modifier
        rebuild()
    }

    private suspend fun rebuild() {
        val container = ModuleContainer()
        modifiers.forEach { it.apply(container) }
        container.applyForEach()

        val modules = _commands.values.firstOrNull()?.modules as? MutableMap<String, Module> ?: mutableMapOf()
        container.modules.values.forEach { it.build(modules) }

        val map: Map<String, Command<out CommandContext>> = modules.values.map { it.commands }.fold(emptyMap()) { acc, map ->
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
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
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger { }

class Pipe(
        val filters: Map<CommandContext<*, *, *>, List<EventFilter<*>>>,
        val preconditions: Map<CommandContext<*, *, *>, List<Precondition<*>>>,
        commands: Map<String, Command<out EventContext>>,
        val prefixes: Map<CommandContext<*, *, *>, Prefix<*, *, *>>,
        private val handler: EventHandler = DefaultHandler,
        private var modifiers: List<ModuleModifier>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {
    private val editMutex = Mutex()
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    private var _commands: Map<String, Command<out EventContext>> = commands
    val commands: Map<String, Command<out EventContext>> get() = _commands

    private suspend inline fun edit(modify: () -> Unit) {
        editMutex.withLock { modify() }
    }

    fun <SOURCECONTEXT> add(source: EventSource<SOURCECONTEXT>): Job {
        return source.events.onEach {
            handle<SOURCECONTEXT, Any?, EventContext>(it, source.context.cast(), source.converter.cast())
        }.catch { logger.catching(it) }.launchIn(this)
    }

    suspend fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> handle(
            event: SOURCECONTEXT,
            context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
            converter: ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    ) = with(handler) { onEvent(event, context, converter) }

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

        val map: Map<String, Command<*>> = modules.values.map { it.commands }.fold(emptyMap()) { acc, map ->
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
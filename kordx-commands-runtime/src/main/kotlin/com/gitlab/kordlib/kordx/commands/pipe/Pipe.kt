package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.flow.ModuleModifier
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.internal.cast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger { }

class Pipe(
        val filters: Map<CommandContext<*, *, *>, List<EventFilter<*>>>,
        val preconditions: Map<CommandContext<*, *, *>, List<Precondition<*>>>,
        commands: Map<String, Command<out EventContext>>,
        val prefixes: Map<CommandContext<*, *, *>, Prefix<*, *, *>>,
        private val handler: EventHandler = DefaultHandler,
        val modifiers: List<ModuleModifier>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    private val _commands: MutableMap<String, Command<out EventContext>> = ConcurrentHashMap(commands)
    val commands: Map<String, Command<out EventContext>> get() = _commands

    fun <SOURCECONTEXT> add(source: EventSource<SOURCECONTEXT>): Job {
        return source.events.onEach {
            handle<SOURCECONTEXT, Any?, EventContext>(it, source.context.cast(), source.converter.cast())
        }.catch { logger.catching(it) }.launchIn(this)
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> handle(
            event: SOURCECONTEXT,
            context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
            converter: ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    ) = with(handler) { onEvent(event, context, converter) }

    suspend operator fun plusAssign(builder: ModuleBuilder<*, *, *>) {
        val modules = _commands.values.firstOrNull()?.modules as? MutableMap<String, Module> ?: mutableMapOf()
        modifiers.forEach { it.modify(builder) }
        builder.build(modules)

        val map: Map<String, Command<*>> = modules.values.map { it.commands }.fold(emptyMap()) { acc, map ->
            map.keys.forEach { require(acc[it] == null) { "command $it is already registered in ${acc[it]!!.module.name}" } }
            acc + map
        }
        map.keys.forEach { require(_commands[it] == null) { "command $it is already registered ${_commands[it]!!.module.name}" } }
        _commands += map
    }

    operator fun minusAssign(command: Command<*>) {
        _commands.remove(command.name, command)
    }

    operator fun minusAssign(module: Module) {
        val keys = _commands.entries.filter { it.value.module == module }.map { it.key }
        keys.forEach { _commands.remove(it) }
    }

}
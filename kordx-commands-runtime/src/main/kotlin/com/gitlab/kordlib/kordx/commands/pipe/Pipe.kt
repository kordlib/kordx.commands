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
import kotlin.coroutines.CoroutineContext

class Pipe(
        val filters: Map<CommandContext<*, *, *>, List<EventFilter<*>>>,
        val preconditions: Map<CommandContext<*, *, *>, List<Precondition<*>>>,
        val commands: MutableMap<String, Command<out EventContext>>,
        val prefixes: Map<CommandContext<*, *, *>, Prefix<*, *, *>>,
        private val handler: EventHandler = DefaultHandler,
        val modifiers: List<ModuleModifier>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = dispatcher + Job()

    fun <SOURCECONTEXT> add(source: EventSource<SOURCECONTEXT>): Job {
        return source.events.onEach {
            handle<SOURCECONTEXT, Any?, EventContext>(it, source.context.cast(), source.converter.cast())
        }.catch { it.printStackTrace() }.launchIn(this)
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> handle(
            event: SOURCECONTEXT,
            context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
            converter: ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    ) = with(handler) { onEvent(event, context, converter) }

    suspend operator fun plusAssign(builder: ModuleBuilder<*, *, *>) {
        val modules = commands.values.firstOrNull()?.modules as? MutableMap<String, Module> ?: mutableMapOf()
        modifiers.forEach { it.modify(builder) }
        builder.build(modules)

        val map: Map<String, Command<*>> = modules.values.map { it.commands }.fold(emptyMap()) { acc, map -> acc + map }
        commands += map
    }

    operator fun minusAssign(command: Command<*>) {
        commands.remove(command.name, command)
    }

    operator fun minusAssign(module: Module) {
        val keys = commands.entries.filter { it.value.module == module }.map { it.key }
        keys.forEach { commands.remove(it) }
    }

}
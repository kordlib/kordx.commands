package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
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
        val filters: Map<CommandContext<*, *, *>, List<EventFilter<*>>> = emptyMap(),
        val preconditions: Map<CommandContext<*, *, *>, List<Precondition<*>>> = emptyMap(),
        val commands: Map<String, Command<out EventContext>> = emptyMap(),
        val prefixes: Map<CommandContext<*, *, *>, Prefix<*, *, *>>,
        private val handler: EventHandler = DefaultHandler,
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

}
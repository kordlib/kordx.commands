package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.pipe.ArgumentsResult
import com.gitlab.kordlib.kordx.commands.pipe.Pipe

interface CommandContext<in SOURCECONTEXT, in ARGUMENTCONTEXT, in EVENTCONTEXT : EventContext>

object CommonContext : CommandContext<Any?, Any?, EventContext>

interface ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> {

    fun supports(context: CommandContext<*, *, *>): Boolean

    suspend fun convert(context: SOURCECONTEXT): ArgumentContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    suspend fun convert(context: ARGUMENTCONTEXT, command: Command<EVENTCONTEXT>, arguments: List<Argument<*, ARGUMENTCONTEXT>>): EVENTCONTEXT
    suspend fun convert(context: EVENTCONTEXT): EventContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>

}

interface ContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> {

    suspend fun respond(message: String): Any?

}

interface ArgumentContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext>
    : ContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT> {

    val text: String
    val argumentContext: ARGUMENTCONTEXT

    suspend fun Pipe.notFound(command: String) {
        respond("$command not found")
    }

    suspend fun Pipe.emptyInvocation(pipe: Pipe) {}

    suspend fun Pipe.rejectArgument(command: Command<EVENTCONTEXT>, words: List<String>, argument: Argument<*, ARGUMENTCONTEXT>, failure: Result.Failure<*>) {
        respond(failure.reason)
    }

    suspend fun Pipe.tooManyWords(command: Command<EVENTCONTEXT>, result: ArgumentsResult.TooManyWords<ARGUMENTCONTEXT>) {
        respond("${command.name} takes more arguments")
    }

}

interface EventContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext>
    : ContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT> {

    val eventContext: EVENTCONTEXT

    suspend fun Pipe.rejectPrecondition(command: Command<EVENTCONTEXT>, failure: PreconditionResult.Fail) {
        respond(failure.message)
    }

}

interface EventContext {
    val command: Command<*>
    val module: Module? get() = command.module

    suspend fun respond(text: String): Any?

}

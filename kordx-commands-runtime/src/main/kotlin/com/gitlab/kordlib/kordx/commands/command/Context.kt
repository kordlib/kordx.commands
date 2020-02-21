package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.pipe.ArgumentsResult
import com.gitlab.kordlib.kordx.commands.pipe.Pipe

interface CommandContext<in SOURCECONTEXT, in ARGUMENTCONTEXT, in EVENTCONTEXT>

object CommonContext : CommandContext<Any?, Any?, Any?>

interface ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT> {

    fun supports(context: CommandContext<*, *, *>): Boolean

    suspend fun convertToArgument(context: SOURCECONTEXT): ArgumentContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    suspend fun convertToEvent(context: EVENTCONTEXT): EventContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    suspend fun convert(context: ARGUMENTCONTEXT, command: Command<EVENTCONTEXT>, arguments: List<Argument<*, ARGUMENTCONTEXT>>): EVENTCONTEXT

}


interface ArgumentContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>  {

    val text: String
    val argumentContext: ARGUMENTCONTEXT

    suspend fun Pipe.notFound(command: String) {}

    suspend fun Pipe.emptyInvocation(pipe: Pipe) {}

    suspend fun Pipe.rejectArgument(
            command: Command<EVENTCONTEXT>,
            words: List<String>,
            argument: Argument<*, ARGUMENTCONTEXT>,
            failure: Result.Failure<*>
    ) {}

    suspend fun Pipe.tooManyWords(command: Command<EVENTCONTEXT>, result: ArgumentsResult.TooManyWords<ARGUMENTCONTEXT>) {}

}

interface EventContextHandler<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT> {

    val eventContext: EVENTCONTEXT

}

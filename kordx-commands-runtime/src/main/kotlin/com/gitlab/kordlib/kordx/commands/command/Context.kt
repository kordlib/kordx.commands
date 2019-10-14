package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult

interface CommandContext<in SOURCECONTEXT, in ARGUMENTCONTEXT, in EVENTCONTEXT : EventContext>

object CommonContext : CommandContext<Any?, Any?, EventContext>

interface ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> {

    fun supports(context: CommandContext<*, *, *>): Boolean
    suspend fun convert(context: SOURCECONTEXT): ARGUMENTCONTEXT
    suspend fun toText(context: ARGUMENTCONTEXT): String
    suspend fun convert(context: ARGUMENTCONTEXT, command: Command<EventContext>, module: Module?, arguments: List<Argument<*, ARGUMENTCONTEXT>>): EVENTCONTEXT


    suspend fun ARGUMENTCONTEXT.notFound(command: String)
    suspend fun ARGUMENTCONTEXT.emptyInvocation()
    suspend fun ARGUMENTCONTEXT.rejectArgument(command: Command<*>, words: List<String>, failure: Result.Failure<*>)
    suspend fun EVENTCONTEXT.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail)
}
interface EventContext {
    val command: Command<*>
    val module: Module?

    suspend fun respond(text: String): Any?

}

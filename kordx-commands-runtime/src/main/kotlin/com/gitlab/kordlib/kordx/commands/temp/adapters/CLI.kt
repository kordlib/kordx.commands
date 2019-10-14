package com.gitlab.kordlib.kordx.commands.temp.adapters

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object CLIContextConverter : ContextConverter<String, String, CLIEvent> {
    override fun supports(context: CommandContext<*, *, *>): Boolean = context is CommonContext
    override suspend fun String.notFound(command: String) {
        System.err.println("$command not found")
    }

    override suspend fun String.rejectArgument(command: Command<*>, words: List<String>, failure: Result.Failure<*>) {
        val stripes = command.name.length + 1 + words.take(failure.atWord).joinToString(" ").length

        System.err.println("""
            $command ${words.joinToString(" ")}
            ${"-".repeat(stripes)}^ ${failure.reason}
            """.trimIndent())
    }

    override suspend fun convert(context: String): String = context
    override suspend fun toText(context: String): String = context
    override suspend fun String.emptyInvocation() = System.err.println("empty invocation, please supply a command")
    override suspend fun CLIEvent.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail) {
        System.err.println(failure.message)
    }
    override suspend fun convert(context: String, command: Command<EventContext>, module: Module?, arguments: List<Argument<*, String>>): CLIEvent {
        return CLIEvent(command, module)
    }
}

class CLIEvent(override val command: Command<*>, override val module: Module?) : EventContext {
    override suspend fun respond(text: String): Any? {
        return println(text)
    }

}

object CLIContext : CommandContext<String, String, CLIEvent>

object CLIEventSource : EventSource<String> {

    override val context: CommandContext<String, *, *>
        get() = CLIContext

    override val converter: ContextConverter<String, *, *>
        get() = CLIContextConverter

    override val events: Flow<String> = flow {
        while (true) {
            emit(readLine() ?: "")
        }
    }

}
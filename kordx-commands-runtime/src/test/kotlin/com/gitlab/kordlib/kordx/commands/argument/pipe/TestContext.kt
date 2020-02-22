package com.gitlab.kordlib.kordx.commands.argument.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.pipe.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TestEventContext(val output: TestOutput, val command: Command<*>) {
    suspend fun respond(text: String): Any? {
        return output.push(EventType.Response(text))
    }
}

object TestContext : CommandContext<String, String, TestEventContext>

sealed class EventType {
    class Response(val text: String) : EventType()
    class NotFound(val command: String) : EventType()
    object EmptyInvocation : EventType()
    class RejectArgument(val command: Command<*>, words: List<String>, failure: Result.Failure<*>) : EventType()
    class RejectPrecondition(val command: Command<*>, val preconditionResult: Boolean) : EventType()
}

class TestOutput {
    private val mutex = Mutex()
    val events: MutableList<EventType> = mutableListOf()

    suspend fun push(eventType: EventType) = mutex.withLock {
        this.events.add(eventType)
    }
}

class TestErrorHandler(private val output: TestOutput) : ErrorHandler<String, String, TestEventContext> {
    override suspend fun Pipe.emptyInvocation(event: String) {
        output.push(EventType.EmptyInvocation)
    }

    override suspend fun Pipe.notFound(event: String, command: String) {
        output.push(EventType.NotFound(command))
    }

    override suspend fun Pipe.rejectArgument(
            event: String,
            command: Command<TestEventContext>,
            words: List<String>,
            argument: Argument<*, String>,
            failure: Result.Failure<*>
    ) {
        output.push(EventType.RejectArgument(command, words, failure))
    }
}

class TestConverter(private val output: TestOutput): ContextConverter<String, String, TestEventContext> {

    override val String.text: String get() = this

    override fun String.toArgumentContext(): String = this

    override fun String.toEventContext(command: Command<TestEventContext>): TestEventContext {
        return TestEventContext(output, command)
    }

}

@Suppress("EXPERIMENTAL_API_USAGE")
class TestEventSource(val output: TestOutput) : EventSource<String> {
    override val context: CommandContext<String, *, *>
        get() = TestContext

    val channel = BroadcastChannel<String>(Channel.CONFLATED)

    override val events: Flow<String> = channel.asFlow()

}
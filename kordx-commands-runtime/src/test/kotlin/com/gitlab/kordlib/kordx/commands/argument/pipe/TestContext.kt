package com.gitlab.kordlib.kordx.commands.argument.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.pipe.ArgumentsResult
import com.gitlab.kordlib.kordx.commands.pipe.EventSource
import com.gitlab.kordlib.kordx.commands.pipe.Pipe
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

object TestContext : CommandContext<Any?, Any?, TestEventContext>

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

class TestEventSource(val output: TestOutput) : EventSource<String> {
    override val context: CommandContext<String, *, *>
        get() = TestContext

    val channel = BroadcastChannel<String>(Channel.CONFLATED)

    override val events: Flow<String> = channel.asFlow()

    override val converter: ContextConverter<String, *, *> = object : ContextConverter<String, String, TestEventContext> {

        override fun supports(context: CommandContext<*, *, *>): Boolean = true

        override suspend fun convertToArgument(context: String): ArgumentContextHandler<String, String, TestEventContext> = object : ArgumentContextHandler<String, String, TestEventContext> {
            override val argumentContext: String
                get() = context

            override val text: String
                get() = context

            override suspend fun Pipe.emptyInvocation(pipe: Pipe) {
                output.push(EventType.EmptyInvocation)
            }

            suspend fun respond(message: String): Any? = output.push(EventType.Response(message))

            override suspend fun Pipe.notFound(command: String) {
                output.push(EventType.NotFound(command))
            }

            override suspend fun Pipe.rejectArgument(command: Command<TestEventContext>, words: List<String>, argument: Argument<*, String>, failure: Result.Failure<*>) {
                output.push(EventType.RejectArgument(command, words, failure))
            }


        }


        override suspend fun convertToEvent(context: TestEventContext): EventContextHandler<String, String, TestEventContext> = object : EventContextHandler<String, String, TestEventContext> {
            override val eventContext: TestEventContext
                get() = context

            suspend fun respond(message: String): Any? = output.push(EventType.Response(message))
        }

        override suspend fun convert(context: String, command: Command<TestEventContext>, arguments: List<Argument<*, String>>): TestEventContext {
            return TestEventContext(output, command)
        }

    }

}
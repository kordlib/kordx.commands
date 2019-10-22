package com.gitlab.kordlib.kordx.commands.argument.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.pipe.EventSource
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TestEventContext(val output: TestOutput, override val command: Command<*>) : EventContext {
    override suspend fun respond(text: String): Any? {
        return output.push(EventType.Response(text))
    }
}

object TestContext : CommandContext<Any?, Any?, TestEventContext>

sealed class EventType {
    class Response(val text: String) : EventType()
    class NotFound(val command: String) : EventType()
    object EmptyInvocation : EventType()
    class RejectArgument(val command: Command<*>, words: List<String>, failure: Result.Failure<*>) : EventType()
    class RejectPrecondition(val command: Command<*>, val preconditionResult: PreconditionResult.Fail) : EventType()
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

        override suspend fun convert(context: String): String = context

        override suspend fun toText(context: String): String = context

        override suspend fun convert(context: String, command: Command<EventContext>, arguments: List<Argument<*, String>>): TestEventContext {
            return TestEventContext(output, command)
        }

        override suspend fun String.notFound(command: String, commands: Map<String, Command<*>>) {
            output.push(EventType.NotFound(command))
        }

        override suspend fun String.emptyInvocation() {
            output.push(EventType.EmptyInvocation)
        }

        override suspend fun String.rejectArgument(command: Command<*>, words: List<String>, failure: Result.Failure<*>) {
            output.push(EventType.RejectArgument(command, words, failure))
        }

        override suspend fun TestEventContext.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail) {
            output.push(EventType.RejectPrecondition(command, failure))
        }

    }
}
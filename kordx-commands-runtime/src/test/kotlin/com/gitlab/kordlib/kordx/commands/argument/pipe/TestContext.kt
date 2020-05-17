@file:Suppress("MemberVisibilityCanBePrivate")

package com.gitlab.kordlib.kordx.commands.argument.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import com.gitlab.kordlib.kordx.commands.model.processor.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult

class TestEventEvent(
        val output: TestOutput,
        override val command: Command<*>,
        override val commands: Map<String, Command<*>>,
        override val processor: CommandProcessor
): CommandEvent {
    suspend fun respond(text: String): Any? {
        return output.push(EventType.Response(text))
    }
}

object TestContext : ProcessorContext<String, String, TestEventEvent>

sealed class EventType {
    class Response(val text: String) : EventType()
    class NotFound(val command: String) : EventType()
    object EmptyInvocation : EventType()
    class RejectArgument(val command: Command<*>, words: List<String>, failure: ArgumentResult.Failure<*>) : EventType()
    class RejectPrecondition(val command: Command<*>, val preconditionResult: Boolean) : EventType()
}

class TestOutput {
    private val mutex = Mutex()
    val events: MutableList<EventType> = mutableListOf()

    suspend fun push(eventType: EventType) = mutex.withLock {
        this.events.add(eventType)
    }
}

class TestErrorHandler(private val output: TestOutput) : ErrorHandler<String, String, TestEventEvent> {
    override suspend fun CommandProcessor.emptyInvocation(event: String) {
        output.push(EventType.EmptyInvocation)
    }

    override suspend fun CommandProcessor.notFound(event: String, command: String) {
        output.push(EventType.NotFound(command))
    }

    override suspend fun CommandProcessor.rejectArgument(
            event: String,
            command: Command<TestEventEvent>,
            words: List<String>,
            argument: Argument<*, String>,
            failure: ArgumentResult.Failure<*>
    ) {
        output.push(EventType.RejectArgument(command, words, failure))
    }
}

class TestConverter(private val output: TestOutput): ContextConverter<String, String, TestEventEvent> {

    override val String.text: String get() = this

    override fun String.toArgumentContext(): String = this

    override fun String.toCommandEvent(data: CommandEventData<TestEventEvent>): TestEventEvent {
        return TestEventEvent(output, data.command, data.commands, data.processor)
    }

}

@Suppress("EXPERIMENTAL_API_USAGE")
class TestEventSource : EventSource<String> {
    override val context: ProcessorContext<String, *, *>
        get() = TestContext

    val channel = BroadcastChannel<String>(Channel.CONFLATED)

    override val events: Flow<String> = channel.asFlow()

}
package com.gitlab.kordlib.kordx.commands.temp.adapters

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.behavior.channel.createMessage
import com.gitlab.kordlib.core.builder.message.MessageCreateBuilder
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

object KordContext : CommandContext<MessageCreateEvent, MessageCreateEvent, KordEvent>

object KordContextConverter : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordEvent> {
    override suspend fun MessageCreateEvent.notFound(command: String) {
        message.channel.createMessage("$command not found (I should probably suggest something)")
    }

    override suspend fun MessageCreateEvent.emptyInvocation() {}

    override suspend fun MessageCreateEvent.rejectArgument(command: Command<*>, words: List<String>, failure: Result.Failure<*>) {
        val stripes = command.name.length + 1 + words.take(failure.atWord).joinToString(" ").length

        message.channel.createMessage("""
            ```
            ${command.name} ${words.joinToString(" ")}
            ${"-".repeat(stripes)}^ ${failure.reason}
            ```
            """.trimIndent())
    }

    override suspend fun KordEvent.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail) {
    }

    override fun supports(context: CommandContext<*, *, *>): Boolean = context is KordContext || context is CommonContext
    override suspend fun convert(context: MessageCreateEvent): MessageCreateEvent = context
    override suspend fun toText(context: MessageCreateEvent): String = context.message.content
    override suspend fun convert(context: MessageCreateEvent, command: Command<EventContext>, module: Module?, arguments: List<Argument<*, MessageCreateEvent>>): KordEvent {
        return KordEvent(command, module, context)
    }
}

class KordEvent(
        override val command: Command<*>,
        override val module: Module?,
        val event: MessageCreateEvent
) : EventContext {
    val kord get() = event.kord

    override suspend fun respond(text: String): Message {
        return event.message.channel.createMessage(text)
    }

    suspend inline fun respond(builder: MessageCreateBuilder.() -> Unit): Message =
            event.message.channel.createMessage(builder)
}

class KordEventSource(val kord: Kord) : EventSource<MessageCreateEvent> {
    override val context: CommandContext<MessageCreateEvent, *, *>
        get() = KordContext

    override val converter: ContextConverter<MessageCreateEvent, *, *>
        get() = KordContextConverter

    override val events: Flow<MessageCreateEvent>
        get() = kord.events.filterIsInstance()
}
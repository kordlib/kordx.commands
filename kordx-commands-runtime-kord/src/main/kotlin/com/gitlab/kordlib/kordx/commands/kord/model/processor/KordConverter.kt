package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.text.StringArgument
import com.gitlab.kordlib.kordx.commands.kord.model.KordEvent
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.*

object KordContextConverter : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordCommandContext> {
    override val MessageCreateEvent.text: String get() =  message.content

    override fun MessageCreateEvent.toArgumentContext(): MessageCreateEvent = this

    override fun MessageCreateEvent.toEventContext(data: EventContextData<KordCommandContext>): KordCommandContext {
        return KordCommandContext(this, data.command, data.commands, data.koin, data.processor)

    }

}

class KordErrorHandler(
        private val suggester: CommandSuggester = CommandSuggester.Companion
) : ErrorHandler<MessageCreateEvent, MessageCreateEvent, KordCommandContext> {

    private suspend inline fun respondError(
            event: MessageCreateEvent,
            command: Command<KordCommandContext>,
            words: List<String>,
            wordPointerIndex: Int,
            message: String
    ) {
        val spacers = command.name.length + 1 + words.take(wordPointerIndex).joinToString(" ").length
        event.message.channel.createMessage("""
                    ```
                    ${command.name} ${words.joinToString(" ")}
                    ${"-".repeat(spacers)}^ $message
                    ```
                """.trimIndent())
    }

    override suspend fun CommandProcessor.emptyInvocation(event: MessageCreateEvent) { /*ignored*/ }

    override suspend fun CommandProcessor.notFound(event: MessageCreateEvent, command: String) {
        val mostProbable = suggester.suggest(command, commands)
        if (mostProbable == null) {
            event.message.channel.createMessage("$command is not an existing command")
            return
        }

        event.message.channel.createMessage("$command not found, did you mean ${mostProbable.name}?")
        val confirmed = with(KordEvent(event)) {
            val text = read(StringArgument)
            text.startsWith("yes", true)
        }

        if (mostProbable.context != KordContext && mostProbable.context != CommonContext) {
            event.message.channel.createMessage("${mostProbable.name} does not accept a Kord context")
            return
        }
        if (confirmed) {
            val correctedText = event.message.content.replaceFirst(command, mostProbable.name)
            val data = event.message.data.copy(content = correctedText)
            val event = MessageCreateEvent(Message(data, event.kord))
            handle(event, KordContext)
        }
    }

    override suspend fun CommandProcessor.rejectArgument(event: MessageCreateEvent, command: Command<KordCommandContext>, words: List<String>, argument: Argument<*, MessageCreateEvent>, failure: Result.Failure<*>) {
        respondError(event, command, words, failure.atWord, failure.reason)
    }

    override suspend fun CommandProcessor.tooManyWords(event: MessageCreateEvent, command: Command<KordCommandContext>, result: ArgumentsResult.TooManyWords<MessageCreateEvent>) {
        respondError(event, command, result.words, result.wordsTaken, "Too many arguments, reached end of command parsing.")
    }
}

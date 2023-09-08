package dev.kordx.commands.kord.model.processor

import dev.kord.core.entity.Message
import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.argument.text.StringArgument
import dev.kordx.commands.kord.model.KordEvent
import dev.kordx.commands.kord.model.context.KordCommandEvent
import dev.kordx.commands.model.context.CommonContext
import dev.kordx.commands.model.processor.CommandEventData
import dev.kordx.commands.model.processor.CommandProcessor
import dev.kordx.commands.model.processor.ContextConverter
import dev.kordx.commands.model.processor.ErrorHandler

/**
 * [ContextConverter] for [KordContext], using the [Message.content] to parse commands.
 */
object KordContextConverter : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordCommandEvent> {
    override val MessageCreateEvent.text: String get() = message.content

    override fun MessageCreateEvent.toArgumentContext(): MessageCreateEvent = this

    override fun MessageCreateEvent.toCommandEvent(data: CommandEventData<KordCommandEvent>): KordCommandEvent {
        return KordCommandEvent(this, data.command, data.commands, data.koin, data.processor)

    }

}

/**
 * Default error handler for the [KordContext]. It will report all errors in the channel the event was typed in and
 * will represent the as embeds, pointing towards the part of the message that created the error.
 * example:
 * ```
 *  Command("add", IntArgument, IntArgument)
 *  > add 4
 *  ´´´
 *  add
 *  ----^ Not enough arguments, reached end of parsing
 *  ´´´
 *  ```
 *
 *  [notFound] Errors will instead suggest the most similar event given by the [suggester].
 */
class KordErrorHandler(
        private val suggester: CommandSuggester = CommandSuggester.Levenshtein
) : ErrorHandler<MessageCreateEvent, MessageCreateEvent, KordCommandEvent> {

    private val backtick = "`"
    private val backtickEscape = "\u200E`"

    private suspend inline fun respondError(
            event: MessageCreateEvent,
            text: String,
            characterIndex: Int,
            message: String
    ) {
        val parsedText = text.take(characterIndex)
        val lastNewlineIndex = parsedText.lastIndexOf("\n")
        val spacers = if (lastNewlineIndex > 0) characterIndex - lastNewlineIndex - 1 //\n itself
        else characterIndex

        event.message.channel.createMessage("""
            <|>```
            <|>${text.replace(backtick, backtickEscape)}
            <|>${"-".repeat(spacers)}^ ${message.replace(backtick, backtickEscape)}
            <|>```
            """.trimMargin("<|>").trim())
    }

    override suspend fun CommandProcessor.emptyInvocation(event: MessageCreateEvent) { /*ignored*/
    }

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
            val event = with(event) {
                MessageCreateEvent(Message(data, kord), guildId, member, shard, supplier)
            }
            handle(event, KordContext)
        }
    }

    override suspend fun CommandProcessor.rejectArgument(
            rejection: ErrorHandler.RejectedArgument<MessageCreateEvent, MessageCreateEvent, KordCommandEvent>
    ) = with(rejection) {
        respondError(event, eventText, atChar, message)
    }

    override suspend fun CommandProcessor.tooManyWords(
            rejection: ErrorHandler.TooManyWords<MessageCreateEvent, KordCommandEvent>
    ) = with(rejection) {
        respondError(
                event,
                eventText,
                eventText.length + 1, //+1 since we're expecting stuff after the text
                "Too many arguments, reached end of command parsing."
        )
    }
}

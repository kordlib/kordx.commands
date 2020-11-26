package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.cache.api.getEntry
import com.gitlab.kordlib.cache.api.put
import com.gitlab.kordlib.cache.api.query
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.core.event.message.MessageUpdateEvent
import com.gitlab.kordlib.kordx.commands.argument.text.StringArgument
import com.gitlab.kordlib.kordx.commands.kord.cache.KordCommandCache
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandEvent
import com.gitlab.kordlib.kordx.commands.model.cache.CommandCache
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.CommandEventData
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import com.gitlab.kordlib.kordx.commands.model.processor.ContextConverter
import com.gitlab.kordlib.kordx.commands.model.processor.ErrorHandler

/**
 * [ContextConverter] for [KordContext], using the [Message.content] to parse commands.
 */
object KordContextConverter : ContextConverter<KordEventAdapter, KordEventAdapter, KordCommandEvent> {
    override val KordEventAdapter.text: String
        get() = content

    override val KordEventAdapter.shouldBeInvoked: Boolean
        get() = event is MessageCreateEvent ||
                event is MessageUpdateEvent && event.new.content.value != event.old?.content

    override suspend fun KordEventAdapter.getCommandCache(): CommandCache =
            if (kord.cache.getEntry<KordCommandCache>() == null) CommandCache.disabled()
            else kord.cache.query<KordCommandCache> { KordCommandCache::messageId eq message.id.value }.singleOrNull()
                    ?: KordCommandCache(kord, message.id.value).also { kord.cache.put(it) }

    override fun KordEventAdapter.toArgumentContext(): KordEventAdapter = this

    override fun KordEventAdapter.toCommandEvent(data: CommandEventData<KordCommandEvent>): KordCommandEvent {
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
) : ErrorHandler<KordEventAdapter, KordEventAdapter, KordCommandEvent> {

    private val backtick = "`"
    private val backtickEscape = "\u200E`"

    private suspend inline fun respondError(
            event: KordEventAdapter,
            text: String,
            characterIndex: Int,
            message: String
    ) {
        val parsedText = text.take(characterIndex)
        val lastNewlineIndex = parsedText.lastIndexOf("\n")
        val spacers = if (lastNewlineIndex > 0) characterIndex - lastNewlineIndex - 1 //\n itself
        else characterIndex

        event.channel.createMessage("""
            <|>```
            <|>${text.replace(backtick, backtickEscape)}
            <|>${"-".repeat(spacers)}^ ${message.replace(backtick, backtickEscape)}
            <|>```
            """.trimMargin("<|>").trim())
    }

    override suspend fun CommandProcessor.emptyInvocation(event: KordEventAdapter) { /*ignored*/
    }

    override suspend fun CommandProcessor.notFound(event: KordEventAdapter, command: String) {
        val mostProbable = suggester.suggest(command, commands)
        if (mostProbable == null) {
            event.channel.createMessage("$command is not an existing command")
            return
        }

        event.channel.createMessage("$command not found, did you mean ${mostProbable.name}?")
        val confirmed = with(event) {
            val text = read(StringArgument)
            text.startsWith("yes", true)
        }

        if (mostProbable.context != KordContext && mostProbable.context != CommonContext) {
            event.channel.createMessage("${mostProbable.name} does not accept a Kord context")
            return
        }
        if (confirmed) {
            val correctedText = event.content.replaceFirst(command, mostProbable.name)
            val updatedEvent = event.replaceContent(correctedText)
            handle(updatedEvent, KordContext)
        }
    }

    override suspend fun CommandProcessor.rejectArgument(
            rejection: ErrorHandler.RejectedArgument<KordEventAdapter, KordEventAdapter, KordCommandEvent>
    ) = with(rejection) {
        respondError(event, eventText, atChar, message)
    }

    override suspend fun CommandProcessor.tooManyWords(
            rejection: ErrorHandler.TooManyWords<KordEventAdapter, KordCommandEvent>
    ) = with(rejection) {
        respondError(
                event,
                eventText,
                eventText.length + 1, //+1 since we're expecting stuff after the text
                "Too many arguments, reached end of command parsing."
        )
    }
}

package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.text.TextArgument
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.kord.CommandSuggester
import com.gitlab.kordlib.kordx.commands.pipe.ArgumentsResult
import com.gitlab.kordlib.kordx.commands.pipe.ContextConverter
import com.gitlab.kordlib.kordx.commands.pipe.ErrorHandler
import com.gitlab.kordlib.kordx.commands.pipe.Pipe
import org.koin.core.Koin

object KordContextConverter : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordEventContext> {
    override val MessageCreateEvent.text: String get() =  message.content

    override fun MessageCreateEvent.toArgumentContext(): MessageCreateEvent = this

    override fun MessageCreateEvent.toEventContext(
            command: Command<KordEventContext>,
            modules: Map<String, Module>,
            commands: Map<String, Command<*>>,
            koin: Koin
    ): KordEventContext {
        return KordEventContext(this, command, commands, koin)
    }

}

class KordErrorHandler(
        private val suggester: CommandSuggester = CommandSuggester.Companion
) : ErrorHandler<MessageCreateEvent, MessageCreateEvent, KordEventContext> {

    private suspend inline fun respondError(
            event: MessageCreateEvent,
            command: Command<KordEventContext>,
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

    override suspend fun Pipe.emptyInvocation(event: MessageCreateEvent) { /*ignored*/ }

    override suspend fun Pipe.notFound(event: MessageCreateEvent, command: String) {
        val mostProbable = suggester.suggest(command, commands)
        if (mostProbable == null) {
            event.message.channel.createMessage("$command is not an existing command")
            return
        }

        event.message.channel.createMessage("$command not found, did you mean ${mostProbable.name}?")
        val confirmed = with(KordEvent(event)) {
            val text = read(TextArgument)
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

    override suspend fun Pipe.rejectArgument(event: MessageCreateEvent, command: Command<KordEventContext>, words: List<String>, argument: Argument<*, MessageCreateEvent>, failure: Result.Failure<*>) {
        respondError(event, command, words, failure.atWord, failure.reason)
    }

    override suspend fun Pipe.tooManyWords(event: MessageCreateEvent, command: Command<KordEventContext>, result: ArgumentsResult.TooManyWords<MessageCreateEvent>) {
        respondError(event, command, result.words, result.wordsTaken, "Too many arguments, reached end of command parsing.")
    }
}

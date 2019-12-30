package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.text.TextArgument
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.kord.CommandSuggester
import com.gitlab.kordlib.kordx.commands.pipe.ArgumentsResult
import com.gitlab.kordlib.kordx.commands.pipe.Pipe

typealias ArgumentHandler = ArgumentContextHandler<MessageCreateEvent, MessageCreateEvent, KordEventContext>
typealias EventHandler = EventContextHandler<MessageCreateEvent, MessageCreateEvent, KordEventContext>

class KordConverter(
        val suggester: CommandSuggester,
        private val eventHandlerSupplier: (KordConverter, KordEventContext) -> EventHandler = { _, context -> DefaultEventHandler(context) },
        private val argumentHandlerSupplier: (KordConverter, MessageCreateEvent) -> ArgumentHandler = { converter, message -> DefaultArgumentHandler(converter, message) }
) : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordEventContext> {
    override fun supports(context: CommandContext<*, *, *>): Boolean {
        return context is KordCommandContext || context is CommonContext
    }

    override suspend fun convert(context: KordEventContext): EventHandler {
        return eventHandlerSupplier(this, context)
    }

    override suspend fun convert(context: MessageCreateEvent, command: Command<KordEventContext>, arguments: List<Argument<*, MessageCreateEvent>>): KordEventContext {
        return KordEventContext(context, command)
    }

    override suspend fun convert(context: MessageCreateEvent): ArgumentHandler {
        return argumentHandlerSupplier(this, context)
    }

    class DefaultEventHandler(val context: KordEventContext) : EventHandler {
        override val eventContext: KordEventContext = context
        override suspend fun respond(message: String): Message = eventContext.respond(message)
    }

    class DefaultArgumentHandler(
            private val converter: KordConverter,
            private val event: MessageCreateEvent
    ) : ArgumentHandler {
        override val text: String
            get() = event.message.content

        override val argumentContext: MessageCreateEvent
            get() = event

        private suspend inline fun respondError(
                command: Command<KordEventContext>,
                words: List<String>,
                wordPointerIndex: Int,
                message: String
        ) {
            val spacers = command.name.length + 1 + words.take(wordPointerIndex).joinToString(" ").length
            respond("""
                    ```
                    ${command.name} ${words.joinToString(" ")}
                    ${"-".repeat(spacers)}^ $message
                    ```
                """.trimIndent())
        }

        override suspend fun respond(message: String): Message = event.message.channel.createMessage(message)

        override suspend fun Pipe.emptyInvocation(pipe: Pipe) { /*ignore*/
        }

        override suspend fun Pipe.tooManyWords(command: Command<KordEventContext>, result: ArgumentsResult.TooManyWords<MessageCreateEvent>) {
            respondError(command, result.words, result.wordsTaken, "Too many arguments, reached end of command parsing.")
        }

        override suspend fun Pipe.notFound(command: String) {
            val mostProbable = converter.suggester.suggest(command, commands) as? Command<EventContext>
            if (mostProbable == null) {
                event.message.channel.createMessage("$command is not an existing command")
                return
            }

            event.message.channel.createMessage("$command not found, did you mean ${mostProbable.name}?")
            val confirmed = with(KordEvent(event)) {
                val text = read(TextArgument)
                text.startsWith("yes", true)
            }

                if (mostProbable.context != KordCommandContext && mostProbable.context != CommonContext) {
                event.message.channel.createMessage("${mostProbable.name} does not accept a Kord context")
                return
            }
            if (confirmed) {
                val correctedText = event.message.content.replaceFirst(command, mostProbable.name)
                val data = event.message.data.copy(content = correctedText)
                val event = MessageCreateEvent(Message(data, event.kord))
                handle(event, KordCommandContext, converter)
            }
        }

        override suspend fun Pipe.rejectArgument(command: Command<KordEventContext>, words: List<String>, argument: Argument<*, MessageCreateEvent>, failure: Result.Failure<*>) {
            respondError(command, words, failure.atWord, failure.reason)
        }

    }
}


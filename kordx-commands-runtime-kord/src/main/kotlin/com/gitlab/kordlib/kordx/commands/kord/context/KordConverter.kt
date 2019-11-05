package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.text.TextArgument
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.kord.CommandSuggester

class KordConverter(
        private val suggester: CommandSuggester
) : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordEventContext> {
    override fun supports(context: CommandContext<*, *, *>): Boolean {
        return context is KordCommandContext || context is CommonContext
    }

    override suspend fun convert(context: MessageCreateEvent): MessageCreateEvent = context

    override suspend fun toText(context: MessageCreateEvent): String = context.message.content

    override suspend fun convert(context: MessageCreateEvent, command: Command<EventContext>, arguments: List<Argument<*, MessageCreateEvent>>): KordEventContext {
        return KordEventContext(context, command)
    }

    override suspend fun MessageCreateEvent.notFound(command: String, commands: Map<String, Command<*>>) {
        val mostProbable = suggester.suggest(command, commands) as? Command<EventContext>
        if (mostProbable == null) {
            message.channel.createMessage("$command is not an existing command")
        } else {
            message.channel.createMessage("$command not found, did you mean ${mostProbable.name}?")
            if (mostProbable.context != KordCommandContext && mostProbable.context != CommonContext) return
            val confirmed = with(KordEvent(this)) {
                val text = read(TextArgument)
                text.startsWith("yes", true)
            }

            if (confirmed) {
                val arguments = mostProbable.arguments as List<Argument<Any?, MessageCreateEvent>>

                //TODO: Kord 0.3.0: replace this with a copy of the event (can't currently access message data) and add the option to feed back into the pipe
                var wordIndex = 0
                val withoutCommand = message.content.split(" ").drop(1)
                val items = mutableListOf<Any?>()
                for (argument in arguments) {
                    when (val result = argument.parse(withoutCommand, wordIndex, this)) {
                        is Result.Success -> {
                            wordIndex += result.wordsTaken
                            items += result.item
                        }
                        is Result.Failure -> {
                            rejectArgument(mostProbable, withoutCommand, result.copy(atWord = result.atWord + wordIndex))
                            return
                        }
                    }
                }

                //reject invocation, too many arguments
                if (wordIndex != withoutCommand.size) {
                    return if (arguments.isEmpty()) rejectArgument(
                            mostProbable,
                            withoutCommand,
                            Result.Failure<Unit>("${mostProbable.name} does not take that many arguments.", wordIndex + 1)
                    )
                    else rejectArgument(
                            mostProbable,
                            withoutCommand,
                            Result.Failure<Unit>("${arguments.last().name} does not take that many arguments.", wordIndex + 1)
                    )
                }

                val eventContext = convert(this, mostProbable, arguments)
                mostProbable.invoke(eventContext, items)
            }

        }
    }

    override suspend fun MessageCreateEvent.emptyInvocation() {
        //display help
    }

    override suspend fun MessageCreateEvent.rejectArgument(command: Command<*>, words: List<String>, failure: Result.Failure<*>) {
        val spacers = command.name.length + 1 + words.take(failure.atWord).joinToString(" ").length

        message.channel.createMessage("""
            ```
            ${command.name} ${words.joinToString(" ")}
            ${"-".repeat(spacers)}^ ${failure.reason}
            ```
            """.trimIndent())

    }

    override suspend fun KordEventContext.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail) {
        message.channel.createMessage(failure.message)
    }

}

package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.kord.CommandSuggester

class KordConverter(private val suggester: CommandSuggester) : ContextConverter<MessageCreateEvent, MessageCreateEvent, KordEvent> {
    override fun supports(context: CommandContext<*, *, *>): Boolean {
        return context is KordContext || context is CommonContext
    }

    override suspend fun convert(context: MessageCreateEvent): MessageCreateEvent = context

    override suspend fun toText(context: MessageCreateEvent): String = context.message.content

    override suspend fun convert(context: MessageCreateEvent, command: Command<EventContext>, arguments: List<Argument<*, MessageCreateEvent>>): KordEvent {
        return KordEvent(context, command)
    }

    override suspend fun MessageCreateEvent.notFound(command: String, commands: Map<String, Command<*>>) {
        val mostProbable = suggester.suggest(command, commands)
        if (mostProbable == null) {
            message.channel.createMessage("$command is not an existing command")
        } else {
            message.channel.createMessage("$command not found, did you mean ${mostProbable.name}?")
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

    override suspend fun KordEvent.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail) {
        message.channel.createMessage(failure.message)
    }

}

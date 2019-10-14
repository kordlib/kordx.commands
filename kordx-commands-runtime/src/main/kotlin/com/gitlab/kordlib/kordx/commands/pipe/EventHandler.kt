package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult


interface EventHandler {

    suspend fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> Pipe.onEvent(
            event: SOURCECONTEXT,
            context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
            converter: ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    )

}

@Suppress("UNCHECKED_CAST")
object DefaultHandler : EventHandler {
    override suspend fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> Pipe.onEvent(
            event: SOURCECONTEXT,
            context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
            converter: ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    ) {
        val filters = filters[context].orEmpty() as List<EventFilter<SOURCECONTEXT>>
        if (!filters.all { it(event) }) return

        val argumentContext = converter.convert(event)
        val text = converter.toText(argumentContext)
        val prefix = (prefixes[context]
                ?: prefixes[CommonContext]) as? Prefix<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>

        val words = if (prefix == null) {
            text.split(" ")
        } else {
            val prefixText = prefix.get(event)
            if (!text.startsWith(prefixText)) return
            text.removePrefix(prefixText).split(" ")
        }

        val command = commands[words.first()] as? Command<EventContext>
                ?: return with(converter) { argumentContext.notFound(words.first()) }
        if (!converter.supports(command.context)) return
        val arguments = command.arguments as List<Argument<Any?, ARGUMENTCONTEXT>>

        var wordIndex = 0
        val withoutCommand = words.drop(1)
        val items = mutableListOf<Any?>()
        for (argument in arguments) {
            when (val result = argument.parse(withoutCommand, wordIndex, argumentContext)) {
                is Result.Success -> {
                    wordIndex += result.wordsTaken
                    items += result.item
                }
                is Result.Failure -> with(converter) {
                    argumentContext.rejectArgument(command, withoutCommand, result.copy(atWord = result.atWord + wordIndex))
                    return
                }
            }
        }

        val eventContext = converter.convert(argumentContext, command, null, arguments)

        val preconditions = preconditions[context].orEmpty() as List<Precondition<EVENTCONTEXT>>
        for (condition in preconditions.sortedBy { it.priority }) {
            val result = condition(eventContext)
            if (result is PreconditionResult.Fail) with(converter) {
                eventContext.rejectPrecondition(command, result)
                return
            }
        }

        command.invoke(eventContext, items)
    }

}

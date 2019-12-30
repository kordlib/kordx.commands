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

sealed class ArgumentsResult<A> {
    data class Success<A>(val items: List<*>) : ArgumentsResult<A>()
    data class TooManyWords<A>(val context: A, val arguments: List<Argument<*, A>>, val words: List<String>, val wordsTaken: Int) : ArgumentsResult<A>()
    data class Failure<A>(val context: A, val failure: Result.Failure<*>, val argument: Argument<*, A>, val arguments: List<Argument<*, A>>, val argumentsTaken: Int, val words: List<String>, val wordsTaken: Int) : ArgumentsResult<A>()
}

@Suppress("UNCHECKED_CAST")
object DefaultHandler : EventHandler {

    private fun <A, B, C : EventContext> Pipe.getPrefix(context: CommandContext<A, B, C>): Prefix<A, B, C>? {
        return (prefixes[context] ?: prefixes[CommonContext]) as? Prefix<A, B, C>
    }

    private suspend fun <A, B, C : EventContext> Prefix<A, B, C>?.words(text: String, event: A): List<String>? {
        return if (this == null) text.split(" ")
        else {
            val prefixText = get(event)
            if (!text.startsWith(prefixText)) null
            else text.removePrefix(prefixText).split(" ")
        }
    }

    private fun <A : EventContext> Pipe.getCommand(words: List<String>): Command<A>? {
        val command = commands[words.first()] ?: return null
        return command as Command<A>
    }

    private suspend fun <A> Pipe.parseArguments(words: List<String>, arguments: List<Argument<*, A>>, context: A): ArgumentsResult<A> {
        var wordIndex = 0
        val items = mutableListOf<Any?>()
        arguments.forEachIndexed { index, argument ->
            when (val result = argument.parse(words, wordIndex, context)) {
                is Result.Success -> {
                    wordIndex += result.wordsTaken
                    items += result.item
                }
                is Result.Failure -> return ArgumentsResult.Failure(context, result, argument, arguments, index, words, wordIndex)
            }
        }

        return ArgumentsResult.Success(items)
    }

    private fun <A : EventContext> Pipe.getPreconditions(context: CommandContext<*, *, A>, command: Command<A>): List<Precondition<A>> {
        return preconditions[context].orEmpty() as List<Precondition<A>> + command.preconditions
    }

    private suspend fun <A : EventContext> calculatePreconditions(preconditions: List<Precondition<A>>, context: A): PreconditionResult.Fail? {
        for (precondition in preconditions.sortedByDescending { it.priority }) {
            when (val result = precondition.invoke(context)) {
                is PreconditionResult.Fail -> return result
            }
        }
        return null
    }

    override suspend fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> Pipe.onEvent(
            event: SOURCECONTEXT,
            context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
            converter: ContextConverter<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
    ) {
        val filters = filters[context].orEmpty() as List<EventFilter<SOURCECONTEXT>>
        if (!filters.all { it(event) }) return

        val argumentHandler = converter.convert(event)
        val prefix = getPrefix(context)

        val words = prefix.words(argumentHandler.text, event) ?: return

        val command = getCommand<EVENTCONTEXT>(words) ?: return with(argumentHandler) { notFound(words.first()) }

        if (!converter.supports(command.context)) return

        val arguments = command.arguments as List<Argument<Any?, ARGUMENTCONTEXT>>

        val (items) = when (val result = parseArguments(words.drop(1), arguments, argumentHandler.argumentContext)) {
            is ArgumentsResult.Success -> result
            is ArgumentsResult.TooManyWords -> return with(argumentHandler) { tooManyWords(command, result) }
            is ArgumentsResult.Failure -> return with(argumentHandler) {
                val newResult = result.failure.copy(atWord = result.failure.atWord + result.wordsTaken)
                rejectArgument(command, words.drop(1), result.argument, newResult)
            }
        }

        val eventContext = converter.convert(argumentHandler.argumentContext, command, arguments)

        val preconditions = getPreconditions(context, command)
        calculatePreconditions(preconditions, eventContext)?.run {
            return with(converter.convert(eventContext)) { rejectPrecondition(command, this@run) }
        }

        command.invoke(eventContext, items)
    }

}

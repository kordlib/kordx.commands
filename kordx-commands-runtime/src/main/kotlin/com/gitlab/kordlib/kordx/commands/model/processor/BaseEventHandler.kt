package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import mu.KotlinLogging


private val baseHandlerLogger = KotlinLogging.logger { }

/**
 * Base event handler implementing the contracts of [EventHandler].
 *
 * @param converter A converter used to convert between [S], [A] and [E].
 * @param handler An error handler used to handle early returns.
 */
open class BaseEventHandler<S, A, E : CommandEvent>(
        override val context: ProcessorContext<S, A, E>,
        protected val converter: ContextConverter<S, A, E>,
        protected val handler: ErrorHandler<S, A, E>
) : EventHandler<S> {

    override suspend fun CommandProcessor.onEvent(event: S) {
        val filters = getFilters(context)
        if (!filters.all { it(event) }) return

        val prefix = prefix.getPrefix(context, event)
        with(converter) {
            if (!event.text.startsWith(prefix)) return
        }

        val words = with(converter) {
            event.text.removePrefix(prefix).split(" ")
        }

        val commandName = words.firstOrNull() ?: return with(handler) { emptyInvocation(event) }
        val command = getCommand(context, commandName) ?: return with(handler) { notFound(event, commandName) }

        @Suppress("UNCHECKED_CAST")
        val arguments = command.arguments as List<Argument<*, A>>

        val argumentContext = with(converter) {
            event.toArgumentContext()
        }

        val eventContext = with(converter) {
            argumentContext.toCommandEvent(CommandEventData(command, command.modules, commands, koin, this@onEvent))
        }

        val preconditions = getPreconditions(context) + command.preconditions

        val passed = preconditions.sortedByDescending { it.priority }.all { it(eventContext) }

        if (!passed) return

        val (items) = when (val result = parseArguments(words.drop(1), arguments, argumentContext)) {
            is ArgumentsResult.Success -> result
            is ArgumentsResult.TooManyWords -> return with(handler) { tooManyWords(event, command, result) }
            is ArgumentsResult.Failure -> return with(handler) {
                val newResult = result.failure.copy(atWord = result.failure.atWord + result.wordsTaken)
                rejectArgument(event, command, words.drop(1), result.argument, newResult)
            }
        }

        try {
            command.invoke(eventContext, items)
        } catch (exception: Exception) {
            baseHandlerLogger.catching(exception)
        }
    }

    /**
     * Parses the [words] with the [arguments] for the [event].
     */
    protected open suspend fun CommandProcessor.parseArguments(
            words: List<String>,
            arguments: List<Argument<*, A>>,
            event: A
    ): ArgumentsResult<A> {
        var wordIndex = 0
        val items = mutableListOf<Any?>()
        arguments.forEachIndexed { index, argument ->
            when (val result = argument.parse(words, wordIndex, event)) {
                is ArgumentResult.Success -> {
                    wordIndex += result.wordsTaken
                    items += result.item
                }
                is ArgumentResult.Failure ->
                    return ArgumentsResult.Failure(event, result, argument, arguments, index, words, wordIndex)
            }
        }

        if (wordIndex != words.size) return ArgumentsResult.TooManyWords(event, arguments, words, wordIndex)
        return ArgumentsResult.Success(items)
    }

}

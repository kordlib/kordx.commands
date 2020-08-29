package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixRule
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

        val text = with(converter) {
            event.text
        }

        val rule = prefix.getPrefixRule(context) ?: PrefixRule.none()

        val prefix = when (val result = rule.consume(text, event)) {
            PrefixRule.Result.Denied -> return
            is PrefixRule.Result.Accepted -> result.prefix
        }

        val withoutPrefix = text.removePrefix(prefix)
        if (withoutPrefix.isBlank()) return with(handler) { emptyInvocation(event) }
        val commandName = withoutPrefix.takeWhile { !it.isWhitespace() }

        val command = getCommand(context, commandName) ?: return with(handler) { notFound(event, commandName) }
        val argString = withoutPrefix.removePrefix(commandName).trimStart()

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

        val trimmedLength = text.length - argString.length
        val (items) = when (val result = parseArguments(trimmedLength, argString, arguments, argumentContext)) {
            is ArgumentsResult.Success -> result
            is ArgumentsResult.TooManyWords -> return with(handler) {
                val rejection = ErrorHandler.TooManyWords(command, event, text)
                tooManyWords(rejection)
            }
            is ArgumentsResult.Failure -> return with(handler) {
                val rejection = ErrorHandler.RejectedArgument(
                        command, event, text, result.atChar, result.argument, result.failure.reason
                )
                rejectArgument(rejection)
            }
        }

        try {
            command.invoke(eventContext, items)
        } catch (exception: Exception) {
            baseHandlerLogger.catching(exception)
            with(handler) { exceptionThrown(event, command, exception) }
        }
    }

    /**
     * Parses the [argumentText] with the [arguments] for the [event].
     */
    protected open suspend fun CommandProcessor.parseArguments(
            trimmedLength: Int,
            argumentText: String,
            arguments: List<Argument<*, A>>,
            event: A
    ): ArgumentsResult<A> {
        var charIndex = 0
        val items = mutableListOf<Any?>()
        arguments.forEachIndexed { index, argument ->
            when (val result = argument.parse(argumentText, charIndex, event)) {
                is ArgumentResult.Success -> {
                    charIndex = result.newIndex
                    items += result.item
                }
                is ArgumentResult.Failure -> return ArgumentsResult.Failure(
                        event, result, argument, arguments, index, argumentText, result.atChar + trimmedLength
                )
            }
        }

        if (charIndex < argumentText.length) return ArgumentsResult.TooManyWords(
                event, arguments, argumentText, argumentText.length + trimmedLength
        )
        return ArgumentsResult.Success(items)
    }

}

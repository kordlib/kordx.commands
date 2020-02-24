package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.Command
import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.PipeContext
import org.koin.core.Koin
import com.gitlab.kordlib.kordx.commands.command.Module

interface EventHandler<S> {

    suspend fun Pipe.onEvent(event: S)

}

interface ContextConverter<S, A, E: CommandContext> {
    val S.text: String

    fun S.toArgumentContext(): A

    fun A.toEventContext(command: Command<E>, modules: Map<String, Module>, commands: Map<String, Command<*>>, koin: Koin): E
}

sealed class ArgumentsResult<A> {
    data class Success<A>(val items: List<*>) : ArgumentsResult<A>()
    data class TooManyWords<A>(val context: A, val arguments: List<Argument<*, A>>, val words: List<String>, val wordsTaken: Int) : ArgumentsResult<A>()
    data class Failure<A>(val context: A, val failure: Result.Failure<*>, val argument: Argument<*, A>, val arguments: List<Argument<*, A>>, val argumentsTaken: Int, val words: List<String>, val wordsTaken: Int) : ArgumentsResult<A>()
}

interface ErrorHandler<S, A, E: CommandContext> {
    suspend fun Pipe.notFound(event: S, command: String) {}

    suspend fun Pipe.emptyInvocation(event: S) {}

    suspend fun Pipe.rejectArgument(
            event: S,
            command: Command<E>,
            words: List<String>,
            argument: Argument<*, A>,
            failure: Result.Failure<*>
    ) {}

    suspend fun Pipe.tooManyWords(event: S, command: Command<E>, result: ArgumentsResult.TooManyWords<A>) {}
}

open class BaseEventHandler<S, A, E: CommandContext>(
        val context: PipeContext<S, A, E>,
        protected val converter: ContextConverter<S, A, E>,
        protected val handler: ErrorHandler<S, A, E>
) : EventHandler<S> {

    override suspend fun Pipe.onEvent(event: S) {
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

        val (items) = when (val result = parseArguments(words.drop(1), arguments, argumentContext)) {
            is ArgumentsResult.Success -> result
            is ArgumentsResult.TooManyWords -> return with(handler) { tooManyWords(event, command, result) }
            is ArgumentsResult.Failure -> return with(handler) {
                val newResult = result.failure.copy(atWord = result.failure.atWord + result.wordsTaken)
                rejectArgument(event, command, words.drop(1), result.argument, newResult)
            }
        }

        val eventContext = with(converter) {
            argumentContext.toEventContext(command, command.modules, commands, koin)
        }

        val preconditions = getPreconditions(context) + command.preconditions

        val passed = preconditions.sortedByDescending { it.priority }.all { it(eventContext) }

        if (passed) {
            command.invoke(eventContext, items)
        } else return
    }

    protected open suspend fun Pipe.parseArguments(words: List<String>, arguments: List<Argument<*, A>>, event: A): ArgumentsResult<A> {
        var wordIndex = 0
        val items = mutableListOf<Any?>()
        arguments.forEachIndexed { index, argument ->
            when (val result = argument.parse(words, wordIndex, event)) {
                is Result.Success -> {
                    wordIndex += result.wordsTaken
                    items += result.item
                }
                is Result.Failure -> return ArgumentsResult.Failure(event, result, argument, arguments, index, words, wordIndex)
            }
        }

        if (wordIndex != words.size) return ArgumentsResult.TooManyWords(event, arguments, words, wordIndex)
        return ArgumentsResult.Success(items)
    }

}

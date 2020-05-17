package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.module.Module
import org.koin.core.Koin
import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.prefix.Prefix
import mu.KotlinLogging
import java.lang.Exception

/**
 * An event handler for a specific [context]. The handler consumes source events [S] and invokes the correct commands
 * if applicable.
 */
interface EventHandler<S> {

    /**
     * The context type token used for handling events.
     */
    val context: ProcessorContext<S, *, *>

    /**
     * Handles the given [event] in the following order:
     *
     * * [EventFilters][EventFilter] for the [context] and [CommonContext] will be run.
     * If any fail, the processing ends early.
     * * The [Prefix] will be compared to the [event]'s message. If it does not match, end early.
     * * The remaining message will be split into space separated words.
     * * The first word will be matched against any [CommandProcessor.commands] name. If none match, end early.
     * * The command's context will be compared to [context]. If they are note equal or the [Command.context] is not
     * [CommonContext], end early.
     * [CommandProcessor.preconditions] of the [context] and [CommonContext] will be matched against the command.
     * If any fail, end early.
     * * The remaining words will be matched against the [Command.arguments], if any return a [ArgumentResult.Failure],
     * end early. If any words remain after parsing, end early.
     * * Finally, [invoke][Command.invoke] the command with the parsed arguments.
     */
    suspend fun CommandProcessor.onEvent(event: S)

}

/**
 * Data passed to create a [CommandEvent].
 *
 * @param command The command that is being invoked.
 * @param modules all modules in the [CommandProcessor].
 * @param commands all commands in the [CommandProcessor].
 * @param koin the koin instance for the event.
 * @param processor the processor that handled the event.
 */
data class CommandEventData<E : CommandEvent>(
        val command: Command<E>,
        val modules: Map<String, Module>,
        val commands: Map<String, Command<*>>,
        val koin: Koin,
        val processor: CommandProcessor
)

/**
 * Converter that helps
 */
interface ContextConverter<S, A, E : CommandEvent> {

    /**
     * Gets a String representation of [S] that mimics the expected format of:
     * "{prefix}{command} {arguments...}"
     */
    val S.text: String

    /**
     * Converts the source event to a type expected by [arguments][Argument].
     */
    fun S.toArgumentContext(): A

    /**
     * Converts the argument context to a [CommandEvent] with the given [data].
     */
    fun A.toCommandEvent(data: CommandEventData<E>): E
}

/**
 * Result from parsing all [arguments][Argument] required by a [Command].
 */
sealed class ArgumentsResult<A> {

    /**
     * All arguments where parsed without problems and generated [items].
     */
    data class Success<A>(val items: List<*>) : ArgumentsResult<A>()

    /**
     * After parsing one or more words remained unused, this is seen as an error.
     *
     * @param context the context used while parsing the arguments.
     * @param arguments the arguments that were used to parse [words].
     * @param words the **complete** set of words, only [wordsTaken] amount of these were parsed,
     * everything else was unused.
     * @param wordsTaken the amount of [words] that were parsed.
     * This value will always be smaller than the [size][List.size] of [words].
     */
    data class TooManyWords<A>(
            val context: A,
            val arguments: List<Argument<*, A>>,
            val words: List<String>,
            val wordsTaken: Int
    ) : ArgumentsResult<A>()

    /**
     * An [argument] in the list of [arguments] returned a [failure]
     * while parsing the set of [words] for a certain [context].
     * The error after parsing [wordsTaken] amount of words.
     *
     * @param argumentsTaken The amount of arguments taken before the [failure] was thrown.
     */
    data class Failure<A>(
            val context: A,
            val failure: ArgumentResult.Failure<*>,
            val argument: Argument<*, A>,
            val arguments: List<Argument<*, A>>,
            val argumentsTaken: Int,
            val words: List<String>,
            val wordsTaken: Int
    ) : ArgumentsResult<A>()
}

/**
 * Handler used to report return cases for a [EventHandler].
 * Informing the user of an error during the parsing of an event should be done here.
 */
interface ErrorHandler<S, A, E : CommandEvent> {

    /**
     * Called when an [event] matched its context's [Prefix] and [CommandProcessor.filters], but was assumed to invoke
     * a [command] that didn't exist.
     *
     * ```
     * event > !pang
     * notFound(event, "pang")
     * ```
     */
    suspend fun CommandProcessor.notFound(event: S, command: String) {}

    /**
     * Called when an [event] matched its context's [Prefix] and [CommandProcessor.filters], but contained no command
     * name.
     *
     * ```
     * event > !
     * emptyInvocation(event)
     * ```
     */
    suspend fun CommandProcessor.emptyInvocation(event: S) {}

    /**
     * Called when an [argument] provided a [failure] when parsing [words] from an [event] for a [command].
     *
     * ```
     * Command("roll" , IntArgument)
     * event > !roll cat
     * rejectArgument(event, command, ["cat"], IntArgument, failure("Expected number.",0))
     * ```
     */
    suspend fun CommandProcessor.rejectArgument(
            event: S,
            command: Command<E>,
            words: List<String>,
            argument: Argument<*, A>,
            failure: ArgumentResult.Failure<*>
    ) {
    }

    /**
     * Called when a processor parsed a command correctly, but ended up with words left unused.
     *
     * ```
     * Command("roll", IntArgument)
     * event > !roll 5 6
     * tooManyWords(event, command, TooManyWords<A>(context, [IntArgument], ["5", "6"], 1))
     * ```
     */
    suspend fun CommandProcessor.tooManyWords(event: S, command: Command<E>, result: ArgumentsResult.TooManyWords<A>) {}
}

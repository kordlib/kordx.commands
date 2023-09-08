package dev.kordx.commands.model.processor

import dev.kordx.commands.argument.Argument
import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.model.command.Command
import dev.kordx.commands.model.command.CommandEvent
import dev.kordx.commands.model.module.Module
import org.koin.core.Koin
import dev.kordx.commands.model.eventFilter.EventFilter
import dev.kordx.commands.model.context.CommonContext
import dev.kordx.commands.model.prefix.Prefix

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
     * @param arguments the arguments that were used to parse [text].
     * @param text the **complete** set of words, only [atChar] amount of these were parsed,
     * everything else was unused.
     * @param atChar the amount of [text] that was parsed.
     * This value will always be smaller than the [size][List.size] of [text].
     */
    data class TooManyWords<A>(
        val context: A,
        val arguments: List<dev.kordx.commands.argument.Argument<*, A>>,
        val text: String,
        val atChar: Int
    ) : ArgumentsResult<A>()

    /**
     * An [argument] in the list of [arguments] returned a [failure]
     * while parsing the [text] for a certain [context].
     *
     * @param argumentsTaken The amount of arguments taken before the [failure] was thrown.
     * @param atChar The char at which the error happened
     */
    data class Failure<A>(
        val context: A,
        val failure: ArgumentResult.Failure<*>,
        val argument: Argument<*, A>,
        val arguments: List<Argument<*, A>>,
        val argumentsTaken: Int,
        val text: String,
        val atChar: Int
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
     * The result of a rejected argument while parsing a command.
     * @param command the command attempted to invoke.
     * @param event the event attempting to invoke the [command].
     * @param eventText the text message from the event.
     * @param argument the argument that failed the parsing.
     * @param message the message generated by the failing [argument].
     * @param atChar the character at which the parsing failed.
     */
    class RejectedArgument<S, A, E : CommandEvent>(
        val command: Command<E>,
        val event: S,
        val eventText: String,
        val atChar: Int,
        val argument: Argument<*, A>,
        val message: String
    )

    /**
     * The result of parsing a command that was invoked with more text than was consumed by the arguments.
     * @param command the command attempted to invoke.
     * @param event the event attempting to invoke the [command].
     * @param eventText the text message from the event.
     */
    class TooManyWords<S, E : CommandEvent>(
            val command: Command<E>,
            val event: S,
            val eventText: String
    )

    /**
     * Called when an [Argument] rejected an input.
     *
     * ```
     * Command("roll" , IntArgument)
     * event > !roll cat
     * RejectedArgument(command, event, "cat", 5, IntArgument, "Expected number.")
     * ```
     *
     * @param rejection The data describing the failure.
     */
    suspend fun CommandProcessor.rejectArgument(rejection: RejectedArgument<S,A,E>) {


    }

    /**
     * Called when a processor parsed a command correctly, but ended up with words left unused.
     *
     * ```
     * Command("roll", IntArgument)
     * event > !roll 5 6
     * tooManyWords(event, command, TooManyWords<A>(context, [IntArgument], ["5", "6"], 8))
     * ```
     */
    suspend fun CommandProcessor.tooManyWords(rejection: TooManyWords<S,E>) {}

    /**
     * Called when the invocation of a [command] for a [event] threw an [Exception].
     */
    suspend fun CommandProcessor.exceptionThrown(event: S, command: Command<E>, exception: Exception){}
}

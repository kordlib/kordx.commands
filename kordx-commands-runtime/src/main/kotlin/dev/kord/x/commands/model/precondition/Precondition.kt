package dev.kord.x.commands.model.precondition

import dev.kord.x.commands.model.command.Command
import dev.kord.x.commands.model.command.CommandEvent
import dev.kord.x.commands.model.context.CommonContext
import dev.kord.x.commands.model.processor.ProcessorContext

/**
 * A predicate used to filter out [CommandEvents][CommandEvent] before they are invoked.
 */
interface Precondition<C : CommandEvent> {

    /**
     * The priority of this precondition, a precondition with a priority higher than another precondition will run
     * first.
     */
    val priority: Long get() = 0

    /**
     * The context for this precondition, only commands with the same [Command.context] will be matched.
     * If the precondition has [CommonContext] then it will match against any context instead.
     */
    val context: ProcessorContext<*, *, C>

    /**
     * Filters the [event], preventing further processing on `false`.
     */
    suspend operator fun invoke(event: C): Boolean
}

/**
 * Defines a [Precondition] for [context] commands. Any command that doesn't match the [filter] will not be invoked.
 *
 * Note that preconditions run *before* arguments get parsed, a command that passed a precondition is therefore
 * not necessarily invoked.
 *
 * @param priority The priority of this precondition compared to other preconditions.
 * Preconditions with a higher priority will be run before others.
 * This can be used to delay potentially expensive preconditions or define a fixed behavior in side effects.
 */
fun <C : CommandEvent> precondition(
    context: ProcessorContext<*, *, C>,
    priority: Long = 0,
    precondition: suspend C.() -> Boolean
): Precondition<C> = object : Precondition<C> {
    override val priority: Long get() = priority
    override val context: ProcessorContext<*, *, C> = context
    override suspend fun invoke(event: C): Boolean = precondition(event)
}

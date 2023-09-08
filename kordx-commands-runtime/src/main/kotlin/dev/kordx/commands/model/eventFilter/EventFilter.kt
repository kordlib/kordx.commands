package dev.kordx.commands.model.eventFilter

import dev.kordx.commands.model.processor.ProcessorContext
import dev.kordx.commands.model.command.Command
import dev.kordx.commands.model.context.CommonContext

/**
 * A filter for [Commands][Command] of a certain [context].
 *
 * Event filters are the 'first line of defense' against any emitted event
 * and happen **before** any other types of checks (this includes matching the prefix and validating the command name).
 * Because of this, it is suggested to make these checks as light as possible to reduce the overhead per event.
 */
interface EventFilter<S> {

    /**
     * The context this filter operates in.
     * This filter will only be applied on the same contexts.
     * If this context is the [CommonContext], this filter will be applied to every event instead.
     */
    val context: ProcessorContext<S, *, *>

    /**
     * Applies this filter to the [event], returning `false` if further processing of the event should stop.
     */
    suspend operator fun invoke(event: S): Boolean
}

/**
 * DSL to create an [EventFilter] for [Commands][Command] of a certain [context].
 * Any event that does not pass the [filter] will be ignored.
 */
fun <S> eventFilter(
        context: ProcessorContext<S, *, *>,
        filter: suspend S.() -> Boolean
) = object : EventFilter<S> {
    override val context: ProcessorContext<S, *, *> = context

    override suspend fun invoke(event: S): Boolean = filter(event)
}

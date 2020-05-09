package com.gitlab.kordlib.kordx.commands.model.eventFilter

import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import com.gitlab.kordlib.kordx.commands.model.command.Command

/**
 * A filter for [Commands][Command] of a certain [context].
 *
 * Event filters are the 'first line of defense' against any emitted event and happen **before** any other types of checks
 * (this includes matching the prefix and validating the command name).
 * Because of this, it is suggested to make these checks as light as possible to reduce the overhead per event.
 */
interface EventFilter<S> {
    val context: ProcessorContext<S, *, *>
    suspend operator fun invoke(event: S): Boolean
}

/**
 * DSL to create an [EventFilter] for [Commands][Command] of a certain [context].
 * Any event that does not pass the [filter] will be ignored.
 */
fun <S> eventFilter(context: ProcessorContext<S, *, *>, filter: suspend S.() -> Boolean) = object : EventFilter<S> {
    override val context: ProcessorContext<S, *, *> = context

    override suspend fun invoke(event: S): Boolean = filter(event)
}

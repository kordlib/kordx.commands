package dev.kordx.commands.model.processor

import dev.kordx.commands.model.context.CommonContext
import kotlinx.coroutines.flow.Flow

/**
 * A source of [events] to be processed by a [context].
 *
 */
interface EventSource<S> {

    /**
     * An unending flow of values, once the flow ends it will not be collected again.
     */
    val events: Flow<S>

    /**
     * The context that should process the [events].
     * If no [EventHandler] for the context was supplied in the [CommandProcessor], an event handler
     * for the [CommonContext] will be used instead.
     */
    val context: ProcessorContext<S, *, *>
}

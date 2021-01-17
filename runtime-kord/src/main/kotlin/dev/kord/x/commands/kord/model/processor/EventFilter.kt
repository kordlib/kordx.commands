package dev.kord.x.commands.kord.model.processor

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.model.eventFilter.EventFilter
import dev.kord.x.commands.model.processor.CommandProcessor

/**
 * Defines an [EventFilter] for [MessageCreateEvent] events.
 * Any event that doesn't match the [predicate] will be ignored by the [CommandProcessor] and won't be parsed.
 */
fun eventFilter(
    predicate: suspend MessageCreateEvent.() -> Boolean
): EventFilter<MessageCreateEvent> =
    dev.kord.x.commands.model.eventFilter.eventFilter(KordContext, predicate)

package dev.kordx.commands.kord.model.processor

import dev.kordx.commands.model.processor.CommandProcessor
import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.model.eventFilter.EventFilter
import dev.kordx.commands.model.eventFilter.eventFilter

/**
 * Defines an [EventFilter] for [MessageCreateEvent] events.
 * Any event that doesn't match the [predicate] will be ignored by the [CommandProcessor] and won't be parsed.
 */
fun eventFilter(
        predicate: suspend MessageCreateEvent.() -> Boolean
): EventFilter<MessageCreateEvent> = eventFilter(KordContext, predicate)

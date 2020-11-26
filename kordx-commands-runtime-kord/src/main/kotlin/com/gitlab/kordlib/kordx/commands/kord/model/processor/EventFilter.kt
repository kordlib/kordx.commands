package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor

/**
 * Defines an [EventFilter] for [MessageCreateEvent] events.
 * Any event that doesn't match the [predicate] will be ignored by the [CommandProcessor] and won't be parsed.
 */
fun eventFilter(
        predicate: suspend KordEventAdapter.() -> Boolean
): EventFilter<KordEventAdapter> =
        com.gitlab.kordlib.kordx.commands.model.eventFilter.eventFilter(KordContext, predicate)

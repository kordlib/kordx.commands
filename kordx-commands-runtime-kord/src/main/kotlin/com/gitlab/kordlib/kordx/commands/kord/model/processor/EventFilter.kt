package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter

fun eventFilter(
        builder: suspend MessageCreateEvent.() -> Boolean
): EventFilter<MessageCreateEvent> = com.gitlab.kordlib.kordx.commands.model.eventFilter.eventFilter(KordContext, builder)

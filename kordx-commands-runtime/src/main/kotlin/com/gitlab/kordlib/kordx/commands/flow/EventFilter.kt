package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.core.event.message.MessageCreateEvent

typealias EventFilter = suspend (MessageCreateEvent) -> Boolean

fun eventFilter(filter: EventFilter) = filter

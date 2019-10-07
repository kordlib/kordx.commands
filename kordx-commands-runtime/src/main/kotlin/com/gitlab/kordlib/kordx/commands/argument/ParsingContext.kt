package com.gitlab.kordlib.kordx.commands.argument

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent

data class ParsingContext(val kord: Kord, val event: MessageCreateEvent)
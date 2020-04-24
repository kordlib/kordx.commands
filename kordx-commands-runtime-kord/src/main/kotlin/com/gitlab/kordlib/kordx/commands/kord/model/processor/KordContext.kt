package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

interface KordContext : ProcessorContext<MessageCreateEvent, MessageCreateEvent, KordCommandContext> {
    companion object : KordContext
}

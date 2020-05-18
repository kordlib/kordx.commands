package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandEvent
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

/**
 * Type token for all Kord command entities.
 */
interface KordContext : ProcessorContext<MessageCreateEvent, MessageCreateEvent, KordCommandEvent> {
    companion object : KordContext
}

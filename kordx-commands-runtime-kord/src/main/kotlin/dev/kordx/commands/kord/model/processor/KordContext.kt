package dev.kordx.commands.kord.model.processor

import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.kord.model.context.KordCommandEvent
import dev.kordx.commands.model.processor.ProcessorContext

/**
 * Type token for all Kord command entities.
 */
interface KordContext : ProcessorContext<MessageCreateEvent, MessageCreateEvent, KordCommandEvent> {
    companion object : KordContext
}

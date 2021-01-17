package dev.kord.x.commands.kord.model.processor

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.kord.model.context.KordCommandEvent
import dev.kord.x.commands.model.processor.ProcessorContext

/**
 * Type token for all Kord command entities.
 */
interface KordContext : ProcessorContext<MessageCreateEvent, MessageCreateEvent, KordCommandEvent> {
    companion object : KordContext
}

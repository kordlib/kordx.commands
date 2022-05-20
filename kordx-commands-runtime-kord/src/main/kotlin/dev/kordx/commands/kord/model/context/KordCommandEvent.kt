package dev.kordx.commands.kord.model.context

import dev.kordx.commands.kord.model.processor.KordContext
import dev.kord.core.Kord
import dev.kord.core.KordObject
import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.kord.model.KordEvent
import dev.kordx.commands.model.command.Command
import dev.kordx.commands.model.command.CommandEvent
import dev.kordx.commands.model.processor.CommandProcessor
import org.koin.core.Koin

/**
 * A [CommandEvent] for the [KordContext].
 */
class KordCommandEvent(
    override val event: MessageCreateEvent,
    override val command: Command<KordCommandEvent>,
    override val commands: Map<String, Command<*>>,
    private val koin: Koin,
    override val processor: CommandProcessor
) : CommandEvent, KordEvent, KordObject {
    fun getKoin(): Koin = koin

    override val module get() = command.module
    override val kord: Kord get() = super.kord
}

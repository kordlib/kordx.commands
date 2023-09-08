package dev.kordx.commands.kord.model.command

import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.model.command.CommandBuilder
import dev.kordx.commands.kord.model.context.KordCommandEvent

typealias KordCommandBuilder = CommandBuilder<MessageCreateEvent, MessageCreateEvent, KordCommandEvent>

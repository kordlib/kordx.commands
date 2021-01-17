package dev.kord.x.commands.kord.model.command

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.kord.model.context.KordCommandEvent
import dev.kord.x.commands.model.command.CommandBuilder

typealias KordCommandBuilder = CommandBuilder<MessageCreateEvent, MessageCreateEvent, KordCommandEvent>

package com.gitlab.kordlib.kordx.commands.kord.model.command

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandContext

typealias KordCommandBuilder = CommandBuilder<MessageCreateEvent, MessageCreateEvent, KordCommandContext>

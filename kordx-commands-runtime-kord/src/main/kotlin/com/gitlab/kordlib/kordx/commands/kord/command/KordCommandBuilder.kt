package com.gitlab.kordlib.kordx.commands.kord.command

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventContext

typealias KordCommandBuilder = CommandBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>

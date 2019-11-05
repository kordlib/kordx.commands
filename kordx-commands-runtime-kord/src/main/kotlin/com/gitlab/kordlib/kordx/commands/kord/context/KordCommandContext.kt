package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.CommandContext

object KordCommandContext : CommandContext<MessageCreateEvent, MessageCreateEvent, KordEventContext>
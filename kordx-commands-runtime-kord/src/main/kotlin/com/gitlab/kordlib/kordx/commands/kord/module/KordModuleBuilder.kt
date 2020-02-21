package com.gitlab.kordlib.kordx.commands.kord.module

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventContext

typealias KordModuleBuilder = ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>

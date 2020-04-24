package com.gitlab.kordlib.kordx.commands.kord.module

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.model.module.module

typealias KordModuleBuilder = ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordCommandContext>

inline fun module(
        name: String,
        crossinline builder: suspend KordModuleBuilder.() -> Unit
) = module(name, KordContext) {
    builder()
}
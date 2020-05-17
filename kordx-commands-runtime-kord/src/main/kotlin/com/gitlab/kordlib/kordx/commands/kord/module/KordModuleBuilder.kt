package com.gitlab.kordlib.kordx.commands.kord.module

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandEvent
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.module.module

typealias KordModuleBuilder = ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordCommandEvent>

/**
 * Creates a [ModuleModifier] with the [KordContext].
 *
 * @param name The name of the module.
 * @param builder the configuration of the module.
 */
inline fun module(
        name: String,
        crossinline builder: suspend KordModuleBuilder.() -> Unit
) : ModuleModifier = module(name, KordContext) {
    builder()
}

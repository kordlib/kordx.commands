package dev.kord.x.commands.kord.module

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.kord.model.context.KordCommandEvent
import dev.kord.x.commands.kord.model.processor.KordContext
import dev.kord.x.commands.model.module.ModuleBuilder
import dev.kord.x.commands.model.module.ModuleModifier
import dev.kord.x.commands.model.module.module

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
): ModuleModifier = module(name, KordContext) {
    builder()
}

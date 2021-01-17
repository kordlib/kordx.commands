package dev.kord.x.commands.kord.module

import dev.kord.x.commands.kord.model.command.KordCommandBuilder
import dev.kord.x.commands.kord.model.processor.KordContext
import dev.kord.x.commands.model.module.CommandSet

/**
 * Defines a [CommandSet] configured by the [builder].
 */
fun commands(
    builder: KordModuleBuilder.() -> Unit
) = dev.kord.x.commands.model.module.commands(KordContext, builder)

/**
 * Defines a [CommandSet] with a single command with the given [name] and configured by the [builder].
 */
fun command(
    name: String,
    builder: KordCommandBuilder.() -> Unit
): CommandSet = dev.kord.x.commands.model.module.command(KordContext, name, builder)

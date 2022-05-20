package dev.kordx.commands.kord.module

import dev.kordx.commands.kord.model.command.KordCommandBuilder
import dev.kordx.commands.kord.model.processor.KordContext
import dev.kordx.commands.model.module.CommandSet
import dev.kordx.commands.model.module.commands
import dev.kordx.commands.model.module.command

/**
 * Defines a [CommandSet] configured by the [builder].
 */
fun commands(
        builder: KordModuleBuilder.() -> Unit
) = commands(KordContext, builder)

/**
 * Defines a [CommandSet] with a single command with the given [name] and configured by the [builder].
 */
fun command(
        name: String,
        builder: KordCommandBuilder.() -> Unit
): CommandSet = command(KordContext, name, builder)

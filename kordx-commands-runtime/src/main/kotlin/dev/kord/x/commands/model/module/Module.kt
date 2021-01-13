package dev.kord.x.commands.model.module

import dev.kord.x.commands.model.command.Command
import dev.kord.x.commands.model.command.CommandEvent
import dev.kord.x.commands.model.metadata.Metadata


/**
 * A container of [commands] with a unique [name].
 *
 * @param metadata the metadata for this module.
 */
class Module(
    val name: String,
    val commands: Map<String, Command<out CommandEvent>>,
    val metadata: Metadata
)


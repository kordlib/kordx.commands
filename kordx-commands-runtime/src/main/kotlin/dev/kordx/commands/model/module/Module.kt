package dev.kordx.commands.model.module

import dev.kordx.commands.model.command.Command
import dev.kordx.commands.model.command.CommandEvent
import dev.kordx.commands.model.metadata.Metadata


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


package com.gitlab.kordlib.kordx.commands.model.module

import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.metadata.Metadata
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext


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


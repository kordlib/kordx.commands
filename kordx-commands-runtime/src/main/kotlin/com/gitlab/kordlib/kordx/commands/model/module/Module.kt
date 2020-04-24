package com.gitlab.kordlib.kordx.commands.model.module

import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.metadata.Metadata
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

inline fun <S, A, C: CommandContext> module(
        name: String,
        context: ProcessorContext<S, A, C>,
        crossinline builder: suspend ModuleBuilder<S, A, C>.() -> Unit
): ModuleModifier = moduleModifier(name) {
    withContext(context) { builder() }
}

class Module(
        val name: String,
        val commands: Map<String, Command<out CommandContext>>,
        val metadata: Metadata
)


package com.gitlab.kordlib.kordx.commands.model.module

import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

fun <S, A, C: CommandContext> commands(
        context: ProcessorContext<S, A, C>,
        builder: ModuleBuilder<S, A, C>.() -> Unit
): CommandSet = object : CommandSet {
    override fun ModuleBuilder<*, *, *>.apply() {
        commands += ModuleBuilder(name, context, metaData, commands).apply(builder).commands
    }
}

fun <S, A, C: CommandContext> command(
        context: ProcessorContext<S, A, C>,
        name: String,
        builder: CommandBuilder<S, A, C>.() -> Unit
): CommandSet = commands(context) { command(name, builder) }

interface CommandSet {
    fun ModuleBuilder<*, *, *>.apply()
}

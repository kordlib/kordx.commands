package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.flow.ModuleModifier
import com.gitlab.kordlib.kordx.commands.flow.moduleModifier

inline fun <S, A, C: CommandContext> module(
        name: String,
        context: PipeContext<S, A, C>,
        crossinline builder: suspend ModuleBuilder<S, A, C>.() -> Unit
): ModuleModifier = moduleModifier(name) {
    withContext(context) { builder() }
}

object EachCommand : Metadata.Key<CommandBuilder<*, *, *>.() -> Unit>

fun <S, A, C: CommandContext> ModuleBuilder<S, A, C>.eachCommand(
        builder: CommandBuilder<*, *, *>.() -> Unit
) {
    when (val previous = metaData[EachCommand]) {
        null -> metaData[EachCommand] = builder
        else -> metaData[EachCommand] = {
            previous()
            builder()
        }
    }
}

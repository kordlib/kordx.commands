package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.flow.ModuleModifier
import com.gitlab.kordlib.kordx.commands.flow.moduleModifier

inline fun <SOURCECONTEXT, ARGUMENTCONTEXT, T : EventContext> module(
        name: String,
        context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, T>,
        crossinline builder: suspend ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T>.() -> Unit
): ModuleModifier = moduleModifier(name) {
    withContext(context) { builder() }
}

object EachCommand : MetaData.Key<CommandBuilder<*, *, *>.() -> Unit>

fun <SOURCECONTEXT, ARGUMENTCONTEXT, T : EventContext> ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T>.eachCommand(
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

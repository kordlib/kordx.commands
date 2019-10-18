package com.gitlab.kordlib.kordx.commands.command

inline fun <SOURCECONTEXT, ARGUMENTCONTEXT, T : EventContext> module(
        name: String,
        context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, T>,
        builder: ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T>.() -> Unit
): ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T> {
    return ModuleBuilder(name, context).apply(builder)
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

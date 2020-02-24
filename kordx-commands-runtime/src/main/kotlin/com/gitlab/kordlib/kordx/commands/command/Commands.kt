package com.gitlab.kordlib.kordx.commands.command

fun <S, A, C: CommandContext> commands(
        context: PipeContext<S, A, C>,
        builder: ModuleBuilder<S, A, C>.() -> Unit
): CommandSet = object : CommandSet {
    override fun ModuleBuilder<*, *, *>.apply() {
        commands += ModuleBuilder(name, context, metaData, commands).apply(builder).commands
    }
}

fun <S, A, C: CommandContext> command(
        context: PipeContext<S, A, C>,
        name: String,
        builder: CommandBuilder<S, A, C>.() -> Unit
): CommandSet = commands(context) { command(name, builder) }

interface CommandSet {
    fun ModuleBuilder<*, *, *>.apply()
}

package com.gitlab.kordlib.kordx.commands.command

fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> commands(
        context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
        builder: ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.() -> Unit
): CommandSet = object : CommandSet {
    override fun ModuleBuilder<*, *, *>.apply() {
        commands += ModuleBuilder(name, context, metaData, commands).apply(builder).commands
    }
}

fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> command(
        context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
        name: String,
        builder: CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.() -> Unit
): CommandSet = commands(context) { command(name, builder) }

interface CommandSet {
    fun ModuleBuilder<*, *, *>.apply()
}

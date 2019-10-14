package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder

@CommandsBuilder
data class ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT : EventContext>(
        val name: String,
        val context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>,
        val metaData: MutableMetaData = MutableMetaData(),
        val commands: MutableList<CommandBuilder<*, *, *>> = mutableListOf()
) {
    fun add(command: CommandBuilder<*, *, *>) {
        commands.add(command)
    }

    fun build(modules: MutableMap<String, Module>) {
        val commands = commands.map { it.name to it.build(modules) }.toMap()
        modules[name] = Module(name, commands, metaData)
    }
}

class Module(
        val name: String,
        val commands: Map<String, Command<out EventContext>>,
        val metaData: MetaData
)

fun <SOURCECONTEXT, ARGUMENTCONTEXT, T : EventContext> ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T>.command(
        name: String,
        builder: CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T>.() -> Unit
): CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, T> {
    return CommandBuilder(name, this.name, context).apply(builder).also { add(it) }
}

fun <NEWSOURCECONTEXT, NEWARGUMENTCONTEXT, NEWEVENTCONTEXT : EventContext> ModuleBuilder<*, *, *>.command(
        name: String,
        context: CommandContext<NEWSOURCECONTEXT, NEWARGUMENTCONTEXT, NEWEVENTCONTEXT>,
        builder: CommandBuilder<NEWSOURCECONTEXT, NEWARGUMENTCONTEXT, NEWEVENTCONTEXT>.() -> Unit
): CommandBuilder<NEWSOURCECONTEXT, NEWARGUMENTCONTEXT, NEWEVENTCONTEXT> {
    return CommandBuilder(name, this.name, context).apply(builder).also { add(it) }
}

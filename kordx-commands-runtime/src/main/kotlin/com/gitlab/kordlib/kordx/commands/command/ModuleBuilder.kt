package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder

@CommandsBuilder
data class ModuleBuilder<S, A, C: CommandContext>(
        val name: String,
        val context: PipeContext<S, A, C>,
        val metaData: MutableMetadata = MutableMetadata(),
        val commands: MutableMap<String, CommandBuilder<*, *, *>> = mutableMapOf()
) {
    fun add(command: CommandBuilder<*, *, *>) {
        require(!commands.containsKey(command.name)) { "a command with name ${command.name} is already present" }
        commands[command.name] = command
    }

    operator fun CommandSet.unaryPlus() = apply()

    inline fun<S,A,C: CommandContext> withContext(context: PipeContext<S,A,C>, builder: ModuleBuilder<S,A,C>.() -> Unit) {
        ModuleBuilder(name, context, metaData, commands).apply(builder)
    }

    fun build(modules: MutableMap<String, Module>) {
        require(!modules.containsKey(name)) { "a module with name $name is already present" }
        modules[name] = Module(name, commands.mapValues { it.value.build(modules) }, metaData)
    }
}

class Module(
        val name: String,
        val commands: Map<String, Command<out CommandContext>>,
        val metadata: Metadata
)

fun <S, A, C: CommandContext> ModuleBuilder<S, A, C>.command(
        name: String,
        builder: CommandBuilder<S, A, C>.() -> Unit
) {
    val command = CommandBuilder(name, this.name, context).apply(builder)
    add(command)
}

fun <NEWS, NEWA, NEWC: CommandContext> ModuleBuilder<*, *, *>.command(
        name: String,
        context: PipeContext<NEWS, NEWA, NEWC>,
        builder: CommandBuilder<NEWS, NEWA, NEWC>.() -> Unit
) {
    val command = CommandBuilder(name, this.name, context).apply(builder)
    add(command)
}

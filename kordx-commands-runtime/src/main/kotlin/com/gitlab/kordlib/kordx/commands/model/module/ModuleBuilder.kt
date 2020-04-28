package com.gitlab.kordlib.kordx.commands.model.module

import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.metadata.MutableMetadata
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import org.koin.core.Koin

@CommandsBuilder
data class ModuleBuilder<S, A, C: CommandContext>(
        val name: String,
        val context: ProcessorContext<S, A, C>,
        val metaData: MutableMetadata = MutableMetadata(),
        val commands: MutableMap<String, CommandBuilder<*, *, *>> = mutableMapOf()
) {
    fun add(command: CommandBuilder<*, *, *>) {
        require(!commands.containsKey(command.name)) { "a command with name ${command.name} is already present" }
        commands[command.name] = command
    }

    operator fun CommandSet.unaryPlus() = apply()

    inline fun<S,A,C: CommandContext> withContext(context: ProcessorContext<S, A, C>, builder: ModuleBuilder<S, A, C>.() -> Unit) {
        ModuleBuilder(name, context, metaData, commands).apply(builder)
    }

    fun build(modules: MutableMap<String, Module>, koin: Koin) {
        require(!modules.containsKey(name)) { "a module with name $name is already present" }
        modules[name] = Module(name, commands.mapValues { it.value.build(modules, koin) }, metaData.toMetaData())
    }
}

fun <S, A, C: CommandContext> ModuleBuilder<S, A, C>.command(
        name: String,
        builder: CommandBuilder<S, A, C>.() -> Unit
) {
    val command = CommandBuilder(name, this.name, context).apply(builder)
    add(command)
}

fun <NEWS, NEWA, NEWC: CommandContext> ModuleBuilder<*, *, *>.command(
        name: String,
        context: ProcessorContext<NEWS, NEWA, NEWC>,
        builder: CommandBuilder<NEWS, NEWA, NEWC>.() -> Unit
) {
    val command = CommandBuilder(name, this.name, context).apply(builder)
    add(command)
}

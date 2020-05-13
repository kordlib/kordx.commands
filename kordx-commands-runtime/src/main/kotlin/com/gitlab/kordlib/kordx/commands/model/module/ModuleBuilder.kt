package com.gitlab.kordlib.kordx.commands.model.module

import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.metadata.MutableMetadata
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import org.koin.core.Koin

inline fun <S, A, C: CommandEvent> module(
        name: String,
        context: ProcessorContext<S, A, C>,
        crossinline builder: suspend ModuleBuilder<S, A, C>.() -> Unit
): ModuleModifier = moduleModifier(name) {
    withContext(context) { builder() }
}

@CommandsBuilder
data class ModuleBuilder<S, A, C: CommandEvent>(
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

    inline fun<S,A,C: CommandEvent> withContext(context: ProcessorContext<S, A, C>, builder: ModuleBuilder<S, A, C>.() -> Unit) {
        ModuleBuilder(name, context, metaData, commands).apply(builder)
    }

    inline fun command(
            name: String,
            builder: CommandBuilder<S, A, C>.() -> Unit
    ) {
        val command = CommandBuilder(name, this.name, context).apply(builder)
        add(command)
    }

    inline fun <NEWS, NEWA, NEWC: CommandEvent> command(
            name: String,
            context: ProcessorContext<NEWS, NEWA, NEWC>,
            builder: CommandBuilder<NEWS, NEWA, NEWC>.() -> Unit
    ) {
        val command = CommandBuilder(name, this.name, context).apply(builder)
        add(command)
    }


    fun build(modules: MutableMap<String, Module>, koin: Koin) {
        require(!modules.containsKey(name)) { "a module with name $name is already present" }
        modules[name] = Module(name, commands.mapValues { it.value.build(modules, koin) }, metaData.toMetaData())
    }
}

package com.gitlab.kordlib.kordx.commands.model.module

import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.metadata.MutableMetadata
import com.gitlab.kordlib.kordx.commands.model.processor.BuildEnvironment
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import org.koin.core.Koin

/**
 * Creates a [ModuleModifier] that applies a [configuration] for a [context] to a [ModuleBuilder].
 */
inline fun <S, A, C : CommandEvent> module(
        name: String,
        context: ProcessorContext<S, A, C>,
        crossinline configuration: suspend ModuleBuilder<S, A, C>.() -> Unit
): ModuleModifier = moduleModifier(name) {
    withContext(context) { configuration() }
}

/**
 * A builder for a [Module].
 *
 * @param name the name of the module
 * @param context the default context for [commands].
 * @param metadata the metadata for the module
 * @param commands the commands [linked][Command.module] to the module.
 */
@CommandsBuilder
data class ModuleBuilder<S, A, C : CommandEvent>(
        val name: String,
        val context: ProcessorContext<S, A, C>,
        val metadata: MutableMetadata = MutableMetadata(),
        val commands: MutableMap<String, CommandBuilder<*, *, *>> = mutableMapOf()
) {

    /**
     * Adds the [command] to the [commands].
     *
     * @throws IllegalArgumentException when a command with that [CommandBuilder.name] already exists.
     */
    fun add(command: CommandBuilder<*, *, *>) {
        require(!commands.containsKey(command.name)) { "a command with name ${command.name} is already present" }
        commands[command.name] = command
    }

    /**
     * Applies a CommandSet to this builder.
     */
    operator fun CommandSet.unaryPlus() = apply()

    /**
     * Applies the given [builder] to this builder under a new default [context].
     */
    inline fun <S, A, C : CommandEvent> withContext(
            context: ProcessorContext<S, A, C>,
            builder: ModuleBuilder<S, A, C>.() -> Unit
    ) {
        ModuleBuilder(name, context, metadata, commands).apply(builder)
    }

    /**
     * Adds a new command under the given [name] configured by the [builder] under the default [context].
     */
    inline fun command(
            name: String,
            builder: CommandBuilder<S, A, C>.() -> Unit
    ) {
        val command = CommandBuilder(name, this.name, context).apply(builder)
        add(command)
    }

    /**
     * Adds a new command under the given [name] configured by the [builder] under a new [context].
     */
    inline fun <NEWS, NEWA, NEWC : CommandEvent> command(
            name: String,
            context: ProcessorContext<NEWS, NEWA, NEWC>,
            builder: CommandBuilder<NEWS, NEWA, NEWC>.() -> Unit
    ) {
        val command = CommandBuilder(name, this.name, context).apply(builder)
        add(command)
    }


    /**
     * Builds the modules, adding it to the [environment].
     *
     * @throws IllegalStateException when a module with the same name already exists.
     */
    fun build(environment: BuildEnvironment) {
        val commands = commands.values
                .flatMap { it.build(environment) }
                .map { it.name to it }
                .toMap()

        val module = Module(name, commands, metadata.toMetaData())
        environment.addModule(module)
    }
}

package dev.kord.x.commands.model.command

import dev.kord.x.commands.model.processor.CommandProcessor
import dev.kord.x.commands.model.module.Module
import org.koin.core.KoinComponent

/**
 * The base context supplied for every command.
 */
interface CommandEvent : KoinComponent {
    /**
     * The command that is currently being invoked.
     */
    val command: Command<*>

    /**
     * The module of the [command].
     */
    val module: Module get() = command.module

    /**
     * All commands currently know by the [CommandProcessor], with their [name][Command.name] as key.
     */
    val commands: Map<String, Command<*>>

    /**
     * All modules currently know by the [CommandProcessor], with their [name][Module.name] as key.
     */
    val modules: Map<String, Module> get() = command.modules

    /**
     * The processor that invoked this event.
     */
    val processor: CommandProcessor
}

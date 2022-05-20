package dev.kordx.commands.model.processor

import dev.kordx.commands.model.command.Command
import dev.kordx.commands.model.exception.DuplicateCommandNameException
import dev.kordx.commands.model.exception.DuplicateModuleNameException
import org.koin.core.Koin

/**
 * Container used to store all build [commands] and [modules] in a [CommandProcessor].
 *
 * @param koin The Koin instance for dependency injection post [CommandProcessor] build.
 */
data class BuildEnvironment(
        val koin: Koin,
        val modules: MutableMap<String, Module> = mutableMapOf(),
        val commands: MutableMap<String, Command<*>> = mutableMapOf()
) {

    /**
     * Adds the [module] to the [modules].
     *
     * @throws DuplicateModuleNameException if a module with the same name is already registered.
     */
    fun addModule(module: Module) {
        if (modules.containsKey(module.name)) throw DuplicateModuleNameException(modules[module.name]!!, module)

        modules[module.name] = module
    }

    /**
     * Adds the [command] to the [commands].
     *
     * @throws DuplicateCommandNameException if a command with the same name is already registered.
     */
    fun addCommand(command: Command<*>) {
        if (commands.containsKey(command.name)) throw DuplicateCommandNameException(commands[command.name]!!, command)

        commands[command.name] = command
    }

}

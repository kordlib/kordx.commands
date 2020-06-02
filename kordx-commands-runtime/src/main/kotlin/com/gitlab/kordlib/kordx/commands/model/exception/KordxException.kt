@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package com.gitlab.kordlib.kordx.commands.model.exception

import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.module.Module

/**
 * Base class for all kordx.commands exceptions.
 */
abstract class KordxException(override val message: String?) : Exception()

/**
 * Exception thrown when an [CommandBuilder] detected an alias that is the same as the [CommandBuilder.name].
 *
 * Kordx.commands expects all command names to be unique, this includes aliases.
 *
 * @param builder The builder that encountered the offending alias.
 * @param aliasName The offending alias.
 */
class ConflictingAliasException(
        val builder: CommandBuilder<*, *, *>,
        val aliasName: String
) : KordxException("""
    Command '${builder.name}' has an alias '$aliasName' that that is identical to the command name.
""".trimIndent())

/**
 * Exception thrown when an [Module] was created with the same name of another module.
 * Kordx.commands expects all module names to be unique.
 *
 * @param module The module that was already registered.
 * @param conflictingModule The module that was going to be added under the same name.
 */
class DuplicateModuleNameException(
        val module: Module,
        val conflictingModule: Module
) : KordxException("""
    Module '${conflictingModule.name}' was registered, but another module with the same name already exists.
""".trimIndent())

/**
 * Exception thrown when an [Command] was created with the same name of another command.
 * Kordx.commands expects all command names to be unique.
 *
 * @param command The command that was already registered.
 * @param conflictingCommand The command that was going to be added under the same name.
 */
class DuplicateCommandNameException(
        val command: Command<*>,
        val conflictingCommand: Command<*>
) : KordxException("""
    Command '${conflictingCommand.name}' was registered, but another command with the same name already exists.
""".trimIndent())

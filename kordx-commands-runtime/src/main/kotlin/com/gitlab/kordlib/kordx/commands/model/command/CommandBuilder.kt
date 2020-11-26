package com.gitlab.kordlib.kordx.commands.model.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.model.cache.CommandCache
import com.gitlab.kordlib.kordx.commands.model.exception.ConflictingAliasException
import com.gitlab.kordlib.kordx.commands.model.metadata.MutableMetadata
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.model.precondition.precondition
import com.gitlab.kordlib.kordx.commands.model.processor.BuildEnvironment
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

private val spaceRegex = Regex("\\s")

/**
 * DSL builder for a [Command].
 * @param name The name of the command, this doubles as the word that invokes the command. No whitespaces allowed.
 * @param moduleName The name of the module this command belongs to.
 * @param context The context this command operates in, used as a type token.
 * @param metaData The metadata for this command.
 * @param preconditions Preconditions for this command.
 * @param aliases the aliases that will be created for this command.
 *
 * @throws IllegalArgumentException when [name] contains whitespace characters.
 */
@CommandsBuilder
class CommandBuilder<S, A, COMMANDCONTEXT : CommandEvent>(
        val name: String,
        val moduleName: String,
        val context: ProcessorContext<S, A, COMMANDCONTEXT>,
        val metaData: MutableMetadata = MutableMetadata(),
        val preconditions: MutableList<Precondition<COMMANDCONTEXT>> = mutableListOf(),
        val aliases: MutableSet<String> = mutableSetOf()
) {

    init {
        require(!name.contains(spaceRegex)) { "Command names should not contain spaces but was $name" }
    }

    /**
     * The behavior of this command on [Command.invoke].
     */
    lateinit var execution: suspend (event: COMMANDCONTEXT, cache: CommandCache?, arguments: List<*>) -> Unit

    /**
     * The arguments this command expects. The length should match the one expected in [execution].
     */
    var arguments: List<Argument<*, A>> = emptyList()

    /**
     * Is the cache enabled? If yes, [CommandCache] is provided at invocation of command.
     */
    var isCacheEnabled: Boolean = false

    /**
     * Adds the [aliases] to this command's aliases.
     */
    fun alias(vararg aliases: String) = this.aliases.addAll(aliases)

    /**
     * Builds the command from the current data and adds it to the [environment].
     */
    fun build(environment: BuildEnvironment): Set<Command<COMMANDCONTEXT>> {
        if (name in aliases) throw ConflictingAliasException(this, name)

        val data = CommandData(
                name,
                moduleName,
                context,
                metaData.toMetaData(),
                arguments,
                environment.modules,
                preconditions,
                environment.koin,
                if (aliases.isEmpty()) AliasInfo.None()
                else AliasInfo.Parent(aliases, environment.commands),
                isCacheEnabled,
                execution
        )

        val children = aliases.map {
            data.copy(name = it, aliasInfo = AliasInfo.Child(name, environment.commands))
        }

        val commands = (children + data).map { Command(it) }

        commands.forEach { environment.addCommand(it) }
        return commands.toSet()
    }

    /**
     * Defines a [Precondition] for this specific command with a given [priority][Precondition.priority].
     */
    fun precondition(
            priority: Long = 0,
            precondition: suspend COMMANDCONTEXT.() -> Boolean
    ) {
        preconditions += precondition(context, priority, precondition)
    }
}

package com.gitlab.kordlib.kordx.commands.model.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.model.module.Module
import com.gitlab.kordlib.kordx.commands.model.metadata.MutableMetadata
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import org.koin.core.Koin
import com.gitlab.kordlib.kordx.commands.model.precondition.precondition

private val spaceRegex = Regex("\\s")

/**
 * DSL builder for a [Command].
 * @param name The name of the command, this doubles as the word that invokes the command. No whitespaces allowed.
 * @param moduleName The name of the module this command belongs to.
 * @param context The context this command operates in, used as a type token.
 * @param metaData The metadata for this command.
 * @param preconditions Preconditions for this command.
 *
 * @throws IllegalArgumentException when [name] contains whitespace characters.
 */
@CommandsBuilder
class CommandBuilder<S, A, COMMANDCONTEXT : CommandEvent>(
        val name: String,
        val moduleName: String,
        val context: ProcessorContext<S, A, COMMANDCONTEXT>,
        val metaData: MutableMetadata = MutableMetadata(),
        val preconditions: MutableList<Precondition<COMMANDCONTEXT>> = mutableListOf()
) {

    init {
        require(!name.contains(spaceRegex)) { "Command names should not contain spaces but was $name" }
    }

    /**
     * The behavior of this command on [Command.invoke].
     */
    lateinit var execution: suspend (COMMANDCONTEXT, List<*>) -> Unit

    /**
     * The arguments this command expects. The length should match the one expected in [execution].
     */
    var arguments: List<Argument<*, A>> = emptyList()

    /**
     * Builds the command from the current data.
     */
    fun build(modules: Map<String, Module>, koin: Koin): Command<COMMANDCONTEXT> {
        val data = CommandData(
                name,
                moduleName,
                context,
                metaData.toMetaData(),
                arguments,
                modules,
                preconditions,
                koin,
                execution
        )
        return Command(data)
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

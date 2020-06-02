package com.gitlab.kordlib.kordx.commands.model.command

/**
 * Information on the command's relation to other aliases.
 * A command can either be a [Parent] of aliases,
 * a [Child] alias of another command,
 * or [not][None] be part of any alias structure.
 */
sealed class AliasInfo<T : CommandEvent> {

    /**
     * The command is not part of any alias structure. It has neither a parent nor children.
     */
    class None<T : CommandEvent> : AliasInfo<T>()

    /**
     * The command is a parent, it contains [children] with the same functionality under a different [Command.name].
     *
     * @param childrenNames the names of the child commands defined in [CommandBuilder.alias].
     * @param commands A map of all registered commands, these are used to resolve the [children].
     */
    class Parent<T : CommandEvent>(
            private val childrenNames: Set<String>,
            private val commands: Map<String, Command<*>>
    ) : AliasInfo<T>() {

        /**
         * The child commands defined by invoking [CommandBuilder.alias]. These are identical copies of the parent
         * with a different [Command.name].
         */
        @Suppress("UNCHECKED_CAST")
        val children: List<Command<T>>
            get() = childrenNames.map { commands.getValue(it) as Command<T> }
    }

    /**
     * The command is a child, it contains a [parent] with the same functionality under a different [Command.name].
     *
     * @param parentName The name of the parent command that created this command.
     * @param commands A map of all registered commands, these are used to resolve the [parent].
     */
    class Child<T : CommandEvent>(
            private val parentName: String,
            private val commands: Map<String, Command<*>>
    ) : AliasInfo<T>() {

        /**
         * The parent command that defined this alias, it is an identical copy of this command with a different name.
         */
        @Suppress("UNCHECKED_CAST")
        val parent: Command<T>
            get() = commands[parentName] as Command<T>

    }

}

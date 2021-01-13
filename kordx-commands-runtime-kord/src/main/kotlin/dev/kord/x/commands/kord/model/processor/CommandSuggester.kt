package dev.kord.x.commands.kord.model.processor

import dev.kord.x.commands.model.command.Command
import info.debatty.java.stringsimilarity.Levenshtein

/**
 * A suggester that suggests commands with a similar name from a map.
 */
interface CommandSuggester {

    /**
     * Suggests a command closest to [command] from the [commands]. Returns null if no command
     * was deemed close enough.
     */
    suspend fun suggest(command: String, commands: Map<String, Command<*>>): Command<*>?

    companion object

    /**
     * Suggester based on the [Levenshtein distance](https://en.wikipedia.org/wiki/Levenshtein_distance).
     */
    object Levenshtein : CommandSuggester {
        private val levenshtein = Levenshtein()

        override suspend fun suggest(
            command: String,
            commands: Map<String, Command<*>>
        ): Command<*>? {
            return commands.values.toList().minByOrNull { levenshtein.distance(command, it.name) }
        }
    }

    /**
     * Suggester that does not suggest any commands.
     */
    object None : CommandSuggester {
        override suspend fun suggest(
            command: String,
            commands: Map<String, Command<*>>
        ): Command<*>? = null
    }
}

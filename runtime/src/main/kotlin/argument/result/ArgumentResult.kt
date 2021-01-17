package dev.kord.x.commands.argument.result

import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.result.ArgumentResult.Failure
import dev.kord.x.commands.argument.result.ArgumentResult.Success

/**
 * The product of an [Argument]'s parsing. A [ArgumentResult] can either be a [Success] or a [Failure]
 */
sealed class ArgumentResult<out T> {
    /**
     * A successful parsing, producing an [item] and the [new index][newIndex] after parsing the item.
     */
    data class Success<T>(val item: T, val newIndex: Int) : ArgumentResult<T>() {
        companion object
    }

    /**
     * A failed parsing, the [Argument] couldn't turn the given string into an item [T],
     * producing a [reason] for failure and a [atChar] indicating
     * at which char of the given string the [Argument] failed parsing.
     */
    data class Failure<T>(val reason: String, val atChar: Int) : ArgumentResult<T>() {
        companion object
    }

    companion object {
        /**
         * Turns the given data in a [ArgumentResult.Success] if [item] is not null, or defers to [failure] otherwise.
         */
        fun <T : Any> notNull(
            item: T?,
            wordsTaken: Int,
            failure: () -> Failure<T>
        ): ArgumentResult<T> = when (item) {
            null -> failure()
            else -> Success(item, wordsTaken)
        }
    }
}

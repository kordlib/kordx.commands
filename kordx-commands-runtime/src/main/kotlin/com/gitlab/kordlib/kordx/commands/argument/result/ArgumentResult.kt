package com.gitlab.kordlib.kordx.commands.argument.result

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult.Failure
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult.Success
import com.gitlab.kordlib.kordx.commands.argument.Argument

/**
 * The product of an [Argument]'s parsing. A [ArgumentResult] can either be a [Success] or a [Failure]
 */
sealed class ArgumentResult<out T> {
    /**
     * A successful parsing, producing an [item] and the amount of [wordsTaken] to parse said item.
     */
    data class Success<T>(val item: T, val wordsTaken: Int) : ArgumentResult<T>() {
        companion object
    }

    /**
     * A failed parsing, the [Argument] couldn't turn the given words into an item [T], producing a [reason] for failure
     * and a specific [atWord] indication at which of the given words the [Argument] failed parsing.
     */
    data class Failure<T>(val reason: String, val atWord: Int) : ArgumentResult<T>() {
        companion object
    }

    companion object {
        /**
         * Turns the given data in a [ArgumentResult.Success] if [item] is not null, or defers to [failure] otherwise.
         */
        fun <T : Any> notNull(item: T?, wordsTaken: Int, failure: () -> Failure<T>): ArgumentResult<T> = when (item) {
            null -> failure()
            else -> Success(item, wordsTaken)
        }
    }
}

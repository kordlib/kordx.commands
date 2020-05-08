package com.gitlab.kordlib.kordx.commands.argument

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.extension.*

/**
 * A parser that takes in a set of words and a [CONTEXT], producing a [ArgumentResult] with a possible generated [T].
 *
 * Arguments come with a set of extension functions to further customize them to your requirements:
 * * [optional] and [withDefault] will replace parsing failures with default values.
 * * [map] and [tryMap] allow mapping to more complex types from more basic arguments.
 * * [filter] and [filterIsInstance] will reduce the scope of allowed values.
 * * [or] can be used to accept one of two arguments in the same position.
 * * [repeated] can be used to accept a range of the same argument.
 *
 */
interface Argument<out T, in CONTEXT> {
    /**
     * The name of the argument, used for user-side
     */
    val name: String

    /**
     * An example of a valid set of words
     */
    val example: String

    /**
     * Parses the given [words], reading the remaining words starting from [fromIndex].
     * Returns a [ArgumentResult] which represents either a failure or success in parsing a certain amount of words.
     */
    suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T>
}

/**
 * Utility class for Arguments that expect one word.
 */
abstract class SingleWordArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        if (words.size <= fromIndex) return ArgumentResult.Failure("expected at least 1 word", 0)
        return parse(words[fromIndex], context)
    }

    abstract suspend fun parse(word: String, context: CONTEXT): ArgumentResult<T>

    protected fun <T> success(value: T): ArgumentResult<T> = ArgumentResult.Success(value, 1)
    protected fun <T> failure(reason: String): ArgumentResult<T> = ArgumentResult.Failure(reason, 0)
}

/**
 * Utility class for Arguments that expect a fixed amount of words.
 */
abstract class FixedLengthArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    abstract val length: Int

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        if (words.size - (fromIndex + 1) < length) return ArgumentResult.Failure("expected at least $length words", 0)
        return parse(words.slice(fromIndex..(fromIndex + length)), context)
    }

    abstract suspend fun parse(words: List<String>, context: CONTEXT): ArgumentResult<T>

    protected fun <T> success(value: T): ArgumentResult<T> = ArgumentResult.Success(value, length)
    protected fun <T> failure(reason: String, atWord: Int = 0): ArgumentResult<T> = ArgumentResult.Failure(reason, atWord)

}

/**
 * Utility class for Arguments that expect any range of words.
 */
abstract class VariableLengthArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return parse(words.drop(fromIndex), context)
    }

    abstract suspend fun parse(words: List<String>, context: CONTEXT): ArgumentResult<T>


    protected fun <T> success(value: T, wordsTaken: Int): ArgumentResult<T> = ArgumentResult.Success(value, wordsTaken)
    protected fun <T> failure(reason: String, atWord: Int = 0): ArgumentResult<T> = ArgumentResult.Failure(reason, atWord)

}

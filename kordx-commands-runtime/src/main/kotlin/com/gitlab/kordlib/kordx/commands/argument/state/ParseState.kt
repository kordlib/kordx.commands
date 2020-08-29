package com.gitlab.kordlib.kordx.commands.argument.state

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult

/**
 * Argument that parses through a [ParseState].
 */
abstract class StateArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    /**
     * Convenience flag to drop all whitespace characters before parsing.
     */
    protected open val dropInitialWhitespace: Boolean = true

    final override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        val state = ParseState(text, fromIndex)
        if (dropInitialWhitespace) state.dropWhiteSpace()
        return state.parse(context)
    }

    /**
     * Parses the state with the [context].
     */
    abstract suspend fun ParseState.parse(context: CONTEXT): ArgumentResult<T>

}

/**
 * A mutable state to facilitate the parsing of [text].
 *
 * Characters can be [consumed][consume] to add them to the [consume] builder and increase the [cursor].
 * Alternatively they can be [dropped][drop] to only increase the [cursor].
 *
 * @param startIndex the character index from which to start parsing the [text].
 * @param consumed the builder used to store [consumed][consume] characters.
 */
class ParseState(
        val text: String,
        private val startIndex: Int,
        val consumed: StringBuilder = StringBuilder()
) {
    /**
     * The current char to be judged.
     *
     * @throws IndexOutOfBoundsException when [cursor] >= [text.length][text].
     */
    val char: Char get() = text[cursor]

    /**
     * Whether the parser has fully parsed the [text], indicated by [cursor] >= [text.length][text].
     */
    val ended: Boolean get() = cursor >= text.length

    /**
     * The distance in characters the [cursor] has moved from the [startIndex].
     */
    val cursorMoved: Int get() = cursor - startIndex

    /**
     * The substring of [text] starting from [cursor].
     */
    val remaining: String get() = if (ended) "" else text.substring(cursor)

    /**
     * The current char index, accepts values only in the range of [0..[text.length][text].
     */
    var cursor: Int = startIndex
        set(value) {
            require(value >= 0) {
                "cursor must be positive but was $value"
            }

            require(value <= text.length) {
                "cursor must be in less than word length (${text.length}) but was $value"
            }

            field = value
        }

    /**
     * Clears the [consumed] characters and resets the [cursor] to [startIndex].
     */
    fun reset() {
        consumed.clear()
        cursor = startIndex
    }

    /**
     * Returns the characters consumed by [action]. Clears the [consumed] afterwards.
     */
    inline fun flush(action: () -> Unit): String {
        consumed.clear()
        action()
        val result = consumed.toString()
        consumed.clear()
        return result
    }

    /**
     * Resets the [cursor] to before the [action].
     */
    inline fun<T> peek(action: () -> T): T {
        val before = cursor
        val result = action()
        cursor = before
        return result
    }

    /**
     * Consumes [amount] characters.
     *
     * @throws IllegalArgumentException if the [amount] would exceed the [text.length][text].
     */
    fun consume(amount: Int) {
        require(cursor + amount <= text.length) {
            "taking $amount characters at the cursor position of " +
                    "$cursor must not exceed the word length of ${text.length}"
        }

        consumed.append(text.substring(cursor, cursor + amount))
        cursor += amount
    }

    /**
     * Consumes 1 character.
     *
     * @throws IllegalArgumentException if the cursor would exceed the [text.length][text].
     */
    fun consume() {
        consumed.append(text[cursor])
        cursor += 1
    }

    /**
     * Drops [amount] characters.
     *
     * @throws IllegalArgumentException if the [amount] would exceed the [text.length][text].
     */
    fun drop(amount: Int) {
        require(cursor + amount <= text.length) {
            "dropping $amount characters at the cursor position of " +
                    "$cursor must not exceed the word length of ${text.length}"
        }

        cursor +=  amount
    }

    /**
     * drops the amount of characters in [literal] if the [remaining] String starts with [literal].
     *
     * @return `true` if the [remaining] String starts with [literal], false otherwise.
     */
    fun drop(literal: String): Boolean {
        val match = remaining.startsWith(literal)
        if (match) drop(literal.length)
        return match
    }

    /**
     * Drops 1 character.
     *
     * @throws IllegalArgumentException if the cursor would exceed the [text.length][text].
     */
    fun drop() {
        cursor += 1
    }

}

/**
 * Drops characters that match the [predicate].
 */
inline fun ParseState.dropWhile(predicate: (Char) -> Boolean) {
    while (!ended && predicate(char)) {
        cursor += 1
    }
}

/**
 * Consumes characters that match the [predicate].
 */
inline fun ParseState.consumeWhile(predicate: (Char) -> Boolean) {
    while (!ended && predicate(char)) {
        consume()
    }
}

/**
 * Consumes characters until [predicate] returns `false`.
 */
inline fun ParseState.consumeUntil(predicate: (Char) -> Boolean) {
    while (!ended && !predicate(char)) {
        consume()
    }
}

/**
 * Drops characters as long as they are whitespace.
 */
fun ParseState.dropWhiteSpace(): Unit = dropWhile { it.isWhitespace() }

/**
 * Consumes a sequence of non-whitespace characters.
 */
fun ParseState.consumeWord(): Unit = consumeUntil { it.isWhitespace() }

/**
 * Consumes all [ParseState.remaining] characters.
 */
fun ParseState.consumeAll(): Unit = consume(text.length - cursor)

/**
 * Creates a [ArgumentResult.Success] with the [ParseState.consumed] characters and [ParseState.cursor].
 */
fun ParseState.success(): ArgumentResult.Success<String> = ArgumentResult.Success(consumed.toString(), cursor)

/**
 * Creates a [ArgumentResult.Success] with the [item] and [ParseState.cursor].
 */
fun<T> ParseState.success(item: T): ArgumentResult.Success<T> = ArgumentResult.Success(item, cursor)

/**
 * Creates a [ArgumentResult.Failure] with the [message] and [ParseState.cursor].
 */
fun <T> ParseState.failure(message: String) = ArgumentResult.Failure<T>(message, cursor)

/**
 * Creates a [ArgumentResult.Failure] indicating more characters were expected.
 */
fun <T> ParseState.unexpectedEnd() = ArgumentResult.Failure<T>("Expected more input but reached end.", cursor)

/**
 * Creates a [ArgumentResult.Failure] indicating some other [literal] was expected.
 */
fun <T> ParseState.expected(literal: String) = ArgumentResult.Failure<T>("Expected '$literal'.", cursor)

/**
 * Creates a [ArgumentResult.Failure] indicating some other [literal] was expected.
 */
fun <T> ParseState.expected(literal: Char) = ArgumentResult.Failure<T>("Expected '$literal'.", cursor)

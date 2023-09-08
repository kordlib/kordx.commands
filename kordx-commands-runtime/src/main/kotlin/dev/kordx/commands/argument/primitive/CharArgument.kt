package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.result.WordResult

internal class InternalCharArgument(override val name: String = "Character") : dev.kordx.commands.argument.SingleWordArgument<Char, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<Char> = when (word.length) {
        1 -> success(word[0])
        else -> failure("Expected a character.")
    }

}

/**
 * Argument that matches against a single word, emitting success when the word is one character long.
 *
 * > Note that due the nature of argument parsing, this argument will never match against whitespace characters.
 */
val CharArgument: dev.kordx.commands.argument.Argument<Char, Any?> = InternalCharArgument()

/**
 * Argument that matches against a single word, emitting success when the word is one character long.
 *
 * > Note that due the nature of argument parsing, this argument will never match against whitespace characters.
 */
@Suppress("FunctionName")
fun CharArgument(name: String): dev.kordx.commands.argument.Argument<Char, Any?> = InternalCharArgument(name)

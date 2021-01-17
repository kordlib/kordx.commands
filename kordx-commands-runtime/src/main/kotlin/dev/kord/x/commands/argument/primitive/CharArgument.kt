package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalCharArgument(override val name: String = "Character") :
    SingleWordArgument<Char, Any?>() {

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
val CharArgument: dev.kord.x.commands.argument.Argument<Char, Any?> = InternalCharArgument()

/**
 * Argument that matches against a single word, emitting success when the word is one character long.
 *
 * > Note that due the nature of argument parsing, this argument will never match against whitespace characters.
 */
@Suppress("FunctionName")
fun CharArgument(name: String): dev.kord.x.commands.argument.Argument<Char, Any?> =
    InternalCharArgument(name)

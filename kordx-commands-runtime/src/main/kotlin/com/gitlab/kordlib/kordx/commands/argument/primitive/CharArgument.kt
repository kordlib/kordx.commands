package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

internal class InternalCharArgument(override val name: String = "Character") : SingleWordArgument<Char, Any?>() {
    override val example: String
        get() = ('a'..'Z').random().toString()

    override suspend fun parse(word: String, context: Any?): ArgumentResult<Char> = when (word.length) {
        1 -> success(word[0])
        else -> failure("Expected a character.")
    }

}

/**
 * Argument that matches against a single word, emitting success when the word is one character long.
 *
 * > Note that due the nature of argument parsing, this argument will never match against whitespace characters.
 */
val CharArgument: Argument<Char, Any?> = InternalCharArgument()

@Suppress("FunctionName")
fun CharArgument(name: String): Argument<Char, Any?> = InternalCharArgument(name)
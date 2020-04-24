package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

/**
 * Argument that matches against a single word, emitting success when the word is one character long.
 *
 * > Note that due the nature of argument parsing, this argument will never match against whitespace characters.
 */
open class CharArgument(override val name: String = "Character") : SingleWordArgument<Char, Any?>() {
    final override val example: String
        get() = ('a'..'Z').random().toString()

    final override suspend fun parse(word: String, context: Any?): Result<Char> = when (word.length) {
        1 -> success(word[0])
        else -> failure("Expected a character.")
    }

    companion object : CharArgument()
}
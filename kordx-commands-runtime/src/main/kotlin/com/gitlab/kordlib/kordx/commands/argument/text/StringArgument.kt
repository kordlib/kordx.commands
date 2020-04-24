package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

/**
 * Argument that accepts any String, returning the matched words joined to a single string, separating the words with a space.
 *
 * > This Argument will consume all remaining words
 */
open class StringArgument(override val name: String = "Text") : VariableLengthArgument<String, Any?>() {

    final override val example: String
        get() = "some words"

    final override suspend fun parse(words: List<String>, context: Any?): Result<String> {
        return if (words.isEmpty()) failure("Expected $name.")
        else success(words.joinToString(" "), words.size)
    }

    companion object : StringArgument()
}
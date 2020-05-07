package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

internal class InternalStringArgument(override val name: String = "Text") : VariableLengthArgument<String, Any?>() {

    override val example: String
        get() = "some words"

    override suspend fun parse(words: List<String>, context: Any?): ArgumentResult<String> {
        return if (words.isEmpty()) failure("Expected $name.")
        else success(words.joinToString(" "), words.size)
    }
}

/**
 * Argument that accepts any String, returning the matched words joined to a single string, separating the words with a space.
 *
 * > This Argument will consume all remaining words
 */
val StringArgument: Argument<String, Any?> = InternalStringArgument()
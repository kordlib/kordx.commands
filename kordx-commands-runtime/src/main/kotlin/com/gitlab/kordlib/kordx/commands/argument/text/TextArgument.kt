package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

open class TextArgument(override val name: String = "Text") : VariableLengthArgument<String, Any?>() {

    final override val example: String
        get() = "some words"

    final override suspend fun parse(words: List<String>, context: Any?): Result<String>  {
        return if (words.isEmpty()) failure("Expected $name.")
        else success(words.joinToString(" "), words.size)
    }

    companion object : TextArgument()
}
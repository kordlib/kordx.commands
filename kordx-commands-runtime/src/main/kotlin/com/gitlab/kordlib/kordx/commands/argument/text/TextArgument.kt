package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.ParsingContext
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

open class TextArgument(override val name: String = "Text") : VariableLengthArgument<String>() {

    final override val example: String
        get() = "any combination of words"

    final override suspend fun parse(words: List<String>, context: ParsingContext): Result<String> =
            success(words.joinToString(" "), words.size)
}
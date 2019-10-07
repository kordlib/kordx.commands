package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.ParsingContext
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

class WordsArgument(override val name: String = "Words") : VariableLengthArgument<List<String>>() {

    override val example: String
        get() = "any combination of words"

    override suspend fun parse(words: List<String>, context: ParsingContext): Result<List<String>>
            = success(words, words.size)

    companion object : WordArgument()
}
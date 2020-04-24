package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

/**
 * Argument that accepts any String, returning the matched words as a list.
 *
 * > This Argument will consume all remaining words
 */
class WordsArgument(override val name: String = "Words") : VariableLengthArgument<List<String>, Any?>() {

    override val example: String
        get() = "any combination of words"

    override suspend fun parse(words: List<String>, context: Any?): Result<List<String>> = success(words, words.size)

    companion object : WordArgument()
}
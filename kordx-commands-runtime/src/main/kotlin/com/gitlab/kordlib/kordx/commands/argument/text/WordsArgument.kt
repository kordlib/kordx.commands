package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument


internal class InternalWordsArgument(override val name: String = "Words") : VariableLengthArgument<List<String>, Any?>() {

    override val example: String
        get() = "any combination of words"

    override suspend fun parse(words: List<String>, context: Any?): ArgumentResult<List<String>> = success(words, words.size)

}

/**
 * Argument that accepts any String, returning the matched words as a list.
 *
 * > This Argument will consume all remaining words
 */
val WordsArgument: Argument<List<String>, Any?> = InternalWordsArgument()


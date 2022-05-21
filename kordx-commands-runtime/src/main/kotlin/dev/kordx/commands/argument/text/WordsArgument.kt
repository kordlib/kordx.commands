package dev.kordx.commands.argument.text

import dev.kordx.commands.argument.Argument
import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.VariableLengthArgument


internal class InternalWordsArgument(
        override val name: String = "Words"
) : dev.kordx.commands.argument.VariableLengthArgument<List<String>, Any?>() {

    override suspend fun parse(
            words: List<String>,
            context: Any?
    ): ArgumentResult<List<String>> = success(words, words.size)

}

/**
 * Argument that accepts any String, returning the matched words as a list.
 *
 * > This Argument will consume all remaining words
 */
val WordsArgument: dev.kordx.commands.argument.Argument<List<String>, Any?> = InternalWordsArgument()


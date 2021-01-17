package dev.kord.x.commands.argument.text

import dev.kord.x.commands.argument.VariableLengthArgument
import dev.kord.x.commands.argument.result.ArgumentResult


internal class InternalWordsArgument(
    override val name: String = "Words"
) : VariableLengthArgument<List<String>, Any?>() {

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
val WordsArgument: dev.kord.x.commands.argument.Argument<List<String>, Any?> =
    InternalWordsArgument()


package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument


internal class InternalListArgument(
        private val separator: String = "|"
) : VariableLengthArgument<List<String>, Any?>() {
    override val name: String = "Separated $separator text"

    override val example: String
        get() = "words $separator separated $separator by $separator"

    override suspend fun parse(
            words: List<String>,
            context: Any?
    ): ArgumentResult<List<String>> = success(words.joinToString(" ").split(separator), words.size)

}

/**
 * Argument that accepts any String, returning the matched words split by "|" as opposed to whitespaces.
 *
 * > This Argument will consume all remaining words
 */
val ListArgument: Argument<List<String>, Any?> = InternalListArgument()

/**
 * Argument that accepts any String, returning the matched words split by the [separator] as opposed to whitespaces.
 *
 * @param separator the lists's separator. `"|"` by default.
 *
 * > This Argument will consume all remaining words
 */
@Suppress("FunctionName")
fun ListArgument(separator: String = "|"): Argument<List<String>, Any?> = InternalListArgument(separator)

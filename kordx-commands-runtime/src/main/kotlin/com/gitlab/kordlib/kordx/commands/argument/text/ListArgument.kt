package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

/**
 * Argument that accepts any String, returning the matched words split by the [separator] as opposed to whitespaces.
 *
 * @param separator the lists's separator. `"|"` by default.
 *
 * > This Argument will consume all remaining words
 */
open class ListArgument(private val separator: String = "|", override val name: String = "Separated $separator text") : VariableLengthArgument<List<String>, Any?>() {

    final override val example: String
        get() = "words $separator separated $separator by $separator"

    final override suspend fun parse(words: List<String>, context: Any?): Result<List<String>> = success(words.joinToString(" ").split(separator), words.size)

    companion object : ListArgument()

}
package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument

open class ListArgument(private val separator: String = "|", override val name: String = "Separated $separator text") : VariableLengthArgument<List<String>, Any?>() {

    final override val example: String
        get() = "words $separator separated $separator by $separator"

    final override suspend fun parse(words: List<String>, context: Any?): Result<List<String>> = success(words.joinToString(" ").split(separator), words.size)

    companion object : ListArgument()

}
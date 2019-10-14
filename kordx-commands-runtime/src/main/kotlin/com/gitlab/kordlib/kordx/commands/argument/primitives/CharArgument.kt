package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

open class CharArgument(override val name: String = "Character") : SingleWordArgument<Char, Any?>() {
    final override val example: String
        get() = ('a'..'Z').random().toString()

    final override suspend fun parse(word: String, context: Any?): Result<Char> = when (word.length) {
        1 -> success(word[0])
        else -> failure("Expected a character.")
    }

    companion object : CharArgument()
}
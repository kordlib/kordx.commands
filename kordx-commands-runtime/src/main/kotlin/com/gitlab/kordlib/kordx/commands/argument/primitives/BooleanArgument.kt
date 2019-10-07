package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.ParsingContext
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

open class BooleanArgument(override val name: String = "Boolean") : SingleWordArgument<Boolean>() {
    final override val example: String
        get() = Random.nextBoolean().toString()

    final override suspend fun parse(word: String, context: ParsingContext): Result<Boolean> = when {
        word.equals("true", true) -> success(true)
        word.equals("false", true) -> success(false)
        else -> failure("Expected true or false.")
    }

    companion object : BooleanArgument()
}
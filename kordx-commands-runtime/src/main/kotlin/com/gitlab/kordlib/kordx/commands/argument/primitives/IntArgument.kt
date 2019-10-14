package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

open class IntArgument(override val name: String = "Number") : SingleWordArgument<Int, Any?>() {
    final override val example: String
        get() = Random.nextInt(-100, 100).toString()

    final override suspend fun parse(word: String, context: Any?): Result<Int> = when (val number = word.toIntOrNull()) {
        null -> failure("Expected a whole number.")
        else -> success(number)
    }

    companion object : IntArgument()
}

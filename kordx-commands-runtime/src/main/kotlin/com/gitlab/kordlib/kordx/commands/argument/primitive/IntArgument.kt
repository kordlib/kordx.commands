package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

class InternalIntArgument(override val name: String = "Number") : SingleWordArgument<Int, Any?>() {
    override val example: String
        get() = Random.nextInt(-100, 100).toString()

    override suspend fun parse(word: String, context: Any?): ArgumentResult<Int> = when (val number = word.toIntOrNull()) {
        null -> failure("Expected a whole number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid integer value.
 */
val IntArgument: Argument<Int, Any?> = InternalIntArgument()

/**
 * Argument that matches against a single world, emitting success when the word is a valid integer value.
 */
@Suppress("FunctionName")
fun IntArgument(name: String) = InternalIntArgument(name)
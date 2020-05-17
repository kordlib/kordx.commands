package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

private const val INT_EXAMPLE_MIN_VALUE = -100
private const val INT_EXAMPLE_MAX_VALUE = 100

internal class InternalIntArgument(override val name: String = "Number") : SingleWordArgument<Int, Any?>() {
    override val example: String
        get() = Random.nextInt(INT_EXAMPLE_MIN_VALUE, INT_EXAMPLE_MAX_VALUE).toString()

    override suspend fun parse(
            word: String,
            context: Any?
    ): ArgumentResult<Int> = when (val number = word.toIntOrNull()) {
        null -> failure("Expected a whole number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid integer value.
 */
val IntArgument: Argument<Int, Any?> = InternalIntArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid integer value.
 */
@Suppress("FunctionName")
fun IntArgument(name: String): Argument<Int, Any?> = InternalIntArgument(name)

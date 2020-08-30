package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import kotlin.random.Random

internal class InternalIntArgument(override val name: String = "Number") : SingleWordArgument<Int, Any?>() {
    override suspend fun parse(
            word: String,
            context: Any?
    ): WordResult<Int> = when (val number = word.toIntOrNull()) {
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

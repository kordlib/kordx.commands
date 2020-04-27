package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

internal class InternalDoubleArgument : SingleWordArgument<Double, Any?>() {
    override val name: String = "Number"

    override val example: String
        get() = Random.nextDouble(-100.0, 100.0).toString()

    override suspend fun parse(word: String, context: Any?): Result<Double> =
            when (val number = word.toDoubleOrNull()) {
                null -> failure("Expected a number.")
                else -> success(number)
            }
}

/**
 * Argument that matches against a single world, emitting success when the word is a valid double value.
 */
val DoubleArgument: Argument<Double, Any?> = InternalDoubleArgument()

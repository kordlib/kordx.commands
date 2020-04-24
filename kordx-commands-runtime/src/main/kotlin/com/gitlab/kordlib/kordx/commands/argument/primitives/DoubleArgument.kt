package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

/**
 * Argument that matches against a single world, emitting success when the word is a valid double value.
 */
open class DoubleArgument(override val name: String = "Number") : SingleWordArgument<Double, Any?>() {
    final override val example: String
        get() = Random.nextDouble(-100.0, 100.0).toString()

    final override suspend fun parse(word: String, context: Any?): Result<Double> =
            when (val number = word.toDoubleOrNull()) {
                null -> failure("Expected a number.")
                else -> success(number)
            }

    companion object : DoubleArgument()
}
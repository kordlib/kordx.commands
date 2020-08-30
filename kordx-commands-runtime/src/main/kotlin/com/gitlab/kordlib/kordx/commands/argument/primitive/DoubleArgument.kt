package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import kotlin.random.Random

internal class InternalDoubleArgument(override val name: String = "Number") : SingleWordArgument<Double, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<Double> =
            when (val number = word.toDoubleOrNull()) {
                null -> failure("Expected a number.")
                else -> success(number)
            }
}

/**
 * Argument that matches against a single world, emitting success when the word is a valid double value.
 */
val DoubleArgument: Argument<Double, Any?> = InternalDoubleArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid double value.
 */
@Suppress("FunctionName")
fun DoubleArgument(name: String): Argument<Double, Any?> = InternalDoubleArgument(name)

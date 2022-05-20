package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.result.WordResult

internal class InternalDoubleArgument(override val name: String = "Number") : dev.kordx.commands.argument.SingleWordArgument<Double, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<Double> =
            when (val number = word.toDoubleOrNull()) {
                null -> failure("Expected a number.")
                else -> success(number)
            }
}

/**
 * Argument that matches against a single world, emitting success when the word is a valid double value.
 */
val DoubleArgument: dev.kordx.commands.argument.Argument<Double, Any?> = InternalDoubleArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid double value.
 */
@Suppress("FunctionName")
fun DoubleArgument(name: String): dev.kordx.commands.argument.Argument<Double, Any?> = InternalDoubleArgument(name)

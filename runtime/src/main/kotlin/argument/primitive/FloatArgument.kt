package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalFloatArgument(override val name: String = "Number") :
    SingleWordArgument<Float, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<Float> =
        when (val number = word.toFloatOrNull()) {
            null -> failure("Expected a number.")
            else -> success(number)
        }
}

/**
 * Argument that matches against a single world, emitting success when the word is a valid [Float] value.
 */
val FloatArgument: dev.kord.x.commands.argument.Argument<Float, Any?> = InternalFloatArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid [Float] value.
 */
@Suppress("FunctionName")
fun FloatArgument(name: String): dev.kord.x.commands.argument.Argument<Float, Any?> =
    InternalFloatArgument(name)

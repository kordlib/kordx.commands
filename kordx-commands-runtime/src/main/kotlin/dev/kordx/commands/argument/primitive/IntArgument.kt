package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.result.WordResult

internal class InternalIntArgument(override val name: String = "Number") : dev.kordx.commands.argument.SingleWordArgument<Int, Any?>() {
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
val IntArgument: dev.kordx.commands.argument.Argument<Int, Any?> = InternalIntArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid integer value.
 */
@Suppress("FunctionName")
fun IntArgument(name: String): dev.kordx.commands.argument.Argument<Int, Any?> = InternalIntArgument(name)

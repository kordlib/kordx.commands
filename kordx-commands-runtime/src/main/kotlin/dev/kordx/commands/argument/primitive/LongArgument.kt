package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.result.WordResult

internal class InternalLongArgument(override val name: String = "Number") : dev.kordx.commands.argument.SingleWordArgument<Long, Any?>() {

    override suspend fun parse(
            word: String,
            context: Any?
    ): WordResult<Long> = when (val number = word.toLongOrNull()) {
        null -> failure("Expected a number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid long value.
 */
val LongArgument: dev.kordx.commands.argument.Argument<Long, Any?> = InternalLongArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid long value.
 */
@Suppress("FunctionName")
fun LongArgument(name: String): dev.kordx.commands.argument.Argument<Long, Any?> = InternalLongArgument(name)

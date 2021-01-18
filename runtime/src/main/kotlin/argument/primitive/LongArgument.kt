package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalLongArgument(
    override val name: String = "Number",
    private val radix: Int = 10
) :
    SingleWordArgument<Long, Any?>() {

    override suspend fun parse(
        word: String,
        context: Any?
    ): WordResult<Long> = when (val number = word.toLongOrNull(radix)) {
        null -> failure("Expected a number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid [Long] value.
 */
val LongArgument: dev.kord.x.commands.argument.Argument<Long, Any?> = InternalLongArgument()

/**
 * Argument with [name] and [radix] that matches against a single world, emitting success when the word is a valid [Long] value.
 */
@Suppress("FunctionName")
fun LongArgument(name: String, radix: Int = 10): dev.kord.x.commands.argument.Argument<Long, Any?> =
    InternalLongArgument(name, radix)

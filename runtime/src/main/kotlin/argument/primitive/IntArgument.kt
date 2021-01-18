package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalIntArgument(
    override val name: String = "Number",
    private val radix: Int = 10
) :
    SingleWordArgument<Int, Any?>() {
    override suspend fun parse(
        word: String,
        context: Any?
    ): WordResult<Int> = when (val number = word.toIntOrNull(radix)) {
        null -> failure("Expected a whole number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid [Int] value.
 */
val IntArgument: dev.kord.x.commands.argument.Argument<Int, Any?> = InternalIntArgument()

/**
 * Argument with [name] and [radix] that matches against a single world, emitting success when the word is a valid [Int] value.
 */
@Suppress("FunctionName")
fun IntArgument(name: String, radix: Int = 10): dev.kord.x.commands.argument.Argument<Int, Any?> =
    InternalIntArgument(name, radix)

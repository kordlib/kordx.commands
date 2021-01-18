package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalShortArgument(override val name: String = "Number", private val radix: Int) :
    SingleWordArgument<Short, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<Short> =
        when (val number = word.toShortOrNull(radix)) {
            null -> failure("Expected a number.")
            else -> success(number)
        }
}

/**
 * Argument that matches against a single world, emitting success when the word is a valid [Short] value.
 */
val ShortArgument: dev.kord.x.commands.argument.Argument<Short, Any?> =
    InternalShortArgument(radix = 10)

/**
 * Argument with [name] and [radix] that matches against a single world, emitting success when the word is a valid [Short] value.
 */
@Suppress("FunctionName")
fun ShortArgument(
    name: String,
    radix: Int = 10
): dev.kord.x.commands.argument.Argument<Short, Any?> =
    InternalShortArgument(name, radix)
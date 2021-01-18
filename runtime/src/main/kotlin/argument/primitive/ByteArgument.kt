package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalByteArgument(
    override val name: String = "Number",
    private val radix: Int = 10
) :
    SingleWordArgument<Byte, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<Byte> =
        when (val number = word.toByteOrNull(radix)) {
            null -> failure("Expected a number.")
            else -> success(number)
        }
}

/**
 * Argument that matches against a single world, emitting success when the word is a valid [Byte] value.
 */
val ByteArgument: dev.kord.x.commands.argument.Argument<Byte, Any?> = InternalByteArgument()

/**
 * Argument with [name] and [radix] that matches against a single world, emitting success when the word is a valid [Byte] value.
 */
@Suppress("FunctionName")
fun ByteArgument(name: String, radix: Int = 10): dev.kord.x.commands.argument.Argument<Byte, Any?> =
    InternalByteArgument(name, radix)
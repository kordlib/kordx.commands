package dev.kord.x.commands.argument.extension

import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.result.ArgumentResult
import dev.kord.x.commands.argument.result.extension.optional
import dev.kord.x.commands.argument.result.extension.orElse

/**
 * Returns an Argument that always succeeds, instead emitting `null` if the supplied Argument fails.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(): Argument<T?, CONTEXT> =
    object : Argument<T?, CONTEXT> by this {

        override suspend fun parse(
            text: String,
            fromIndex: Int,
            context: CONTEXT
        ): ArgumentResult<T?> {
            return this@optional.parse(text, fromIndex, context).optional(fromIndex)
        }

    }

/**
 * Returns an Argument that always succeeds, instead emitting [default] if the supplied Argument fails.
 */
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(
    default: T
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@optional.parse(text, fromIndex, context).orElse(fromIndex, default)
    }
}

/**
 * Returns an Argument that always succeeds, instead emitting [default] if the supplied Argument fails.
 */
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(
    default: suspend CONTEXT.() -> T
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@optional.parse(text, fromIndex, context).orElse(fromIndex, default(context))
    }
}

package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.optional
import com.gitlab.kordlib.kordx.commands.argument.result.extension.orElse

/**
 * Returns an Argument that always succeeds, instead emitting `null` if the supplied Argument fails.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(): Argument<T?, CONTEXT> = object : Argument<T?, CONTEXT> by this {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T?> {
        return this@optional.parse(words, fromIndex, context).optional()
    }

}

/**
 * Returns an Argument that always succeeds, instead emitting [default] if the supplied Argument fails.
 */
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(
        default: T
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@optional.parse(words, fromIndex, context).orElse(default)
    }
}

/**
 * Returns an Argument that always succeeds, instead emitting [default] if the supplied Argument fails.
 */
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(
        default: suspend CONTEXT.() -> T
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@optional.parse(words, fromIndex, context).orElse(default(context))
    }
}

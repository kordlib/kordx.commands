package com.gitlab.kordlib.kordx.commands.argument.extensions

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.optional
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.orElse

/**
 * Returns an Argument that always succeeds. Instead emitting `null` if the supplied Argument fails.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(): Argument<T?, CONTEXT> = object :
        Argument<T?, CONTEXT> by this as Argument<T?, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T?> {
        return this@optional.parse(words, fromIndex, context).optional()
    }

}

/**
 * Returns an Argument that always succeeds. Instead emitting [default] if the supplied Argument fails.
 */
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(default: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@optional.parse(words, fromIndex, context).orElse(default(context))
    }
}

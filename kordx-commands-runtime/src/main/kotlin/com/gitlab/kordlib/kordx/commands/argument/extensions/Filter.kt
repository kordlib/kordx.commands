package com.gitlab.kordlib.kordx.commands.argument.extensions

import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.FilterResult
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.filter
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.filterNotNull

/**
 * Returns an Argument that filters all success emits through the [filter], only emitting success on values returning [FilterResult.Pass].
 */
fun <T, CONTEXT> Argument<T, CONTEXT>.filter(
        filter: suspend CONTEXT.(T) -> FilterResult
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@filter.parse(words, fromIndex, context).filter { filter(context, it) }
    }
}

/**
 * Returns an Argument that filters all success emits through the [filter], only emitting success on non-null values.
 * Will emit failure with the given [failMessage] on null values.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.filterNotNull(
        failMessage: String
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@filterNotNull.parse(words, fromIndex, context).filterNotNull(failMessage)
    }
}

/**
 * Returns an Argument that filters all success emits through the [filter], only emitting success on non-null values.
 * Will emit failure with the given [failMessage] on null values.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.filterNotNull(
        failMessage: suspend (T?) -> String
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@filterNotNull.parse(words, fromIndex, context).filterNotNull { failMessage(it) }
    }
}

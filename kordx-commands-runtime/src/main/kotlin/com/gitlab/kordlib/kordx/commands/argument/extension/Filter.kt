package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.*

/**
 * Returns an Argument that filters all success emits through the [filter],
 * only emitting success on values returning [FilterResult.Pass].
 */
fun <T, CONTEXT> Argument<T, CONTEXT>.filter(
        filter: suspend CONTEXT.(T) -> FilterResult
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filter.parse(text, fromIndex, context).filter(fromIndex) { filter(context, it) }
    }
}

/**
 * Returns an Argument that filters all success emits through the [filter],
 * only emitting success on values returning [FilterResult.Pass].
 */
inline fun <reified T, CONTEXT> Argument<*, CONTEXT>.filterIsInstance(
        failMessage: String
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> {

    override val name: String
        get() = this@filterIsInstance.name

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filterIsInstance.parse(text, fromIndex, context).filterIsInstance(fromIndex, failMessage)
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

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filterNotNull.parse(text, fromIndex, context).filterNotNull(failMessage)
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

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filterNotNull.parse(text, fromIndex, context).filterNotNull { failMessage(it) }
    }
}

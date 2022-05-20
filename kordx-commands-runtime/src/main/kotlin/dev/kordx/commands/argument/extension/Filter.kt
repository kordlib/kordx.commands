package dev.kordx.commands.argument.extension

import dev.kordx.commands.argument.*
import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.result.extension.*

/**
 * Returns an Argument that filters all success emits through the [filter],
 * only emitting success on values returning [FilterResult.Pass].
 */
fun <T, CONTEXT> dev.kordx.commands.argument.Argument<T, CONTEXT>.filter(
        filter: suspend CONTEXT.(T) -> FilterResult
): dev.kordx.commands.argument.Argument<T, CONTEXT> = object : dev.kordx.commands.argument.Argument<T, CONTEXT> by this {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filter.parse(text, fromIndex, context).filter(fromIndex) { filter(context, it) }
    }
}

/**
 * Returns an Argument that filters all success emits through the [filter],
 * only emitting success on values returning [FilterResult.Pass].
 */
inline fun <reified T, CONTEXT> dev.kordx.commands.argument.Argument<*, CONTEXT>.filterIsInstance(
        failMessage: String
): dev.kordx.commands.argument.Argument<T, CONTEXT> = object : dev.kordx.commands.argument.Argument<T, CONTEXT> {

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
fun <T : Any, CONTEXT> dev.kordx.commands.argument.Argument<T?, CONTEXT>.filterNotNull(
        failMessage: String
): dev.kordx.commands.argument.Argument<T, CONTEXT> = object : dev.kordx.commands.argument.Argument<T, CONTEXT> by this as dev.kordx.commands.argument.Argument<T, CONTEXT> {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filterNotNull.parse(text, fromIndex, context).filterNotNull(failMessage)
    }
}

/**
 * Returns an Argument that filters all success emits through the [filter], only emitting success on non-null values.
 * Will emit failure with the given [failMessage] on null values.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> dev.kordx.commands.argument.Argument<T?, CONTEXT>.filterNotNull(
        failMessage: suspend (T?) -> String
): dev.kordx.commands.argument.Argument<T, CONTEXT> = object : dev.kordx.commands.argument.Argument<T, CONTEXT> by this as dev.kordx.commands.argument.Argument<T, CONTEXT> {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@filterNotNull.parse(text, fromIndex, context).filterNotNull { failMessage(it) }
    }
}

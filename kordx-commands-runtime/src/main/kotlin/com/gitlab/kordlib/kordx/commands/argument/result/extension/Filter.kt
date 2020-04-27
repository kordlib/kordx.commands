package com.gitlab.kordlib.kordx.commands.argument.result.extension

import com.gitlab.kordlib.kordx.commands.argument.result.Result


/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is [T],
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is not [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Result<*>.filterIsInstance(failMessage: String): Result<T> = when (val result = this) {
    is Result.Success -> when (result.item) {
        is T -> result as Result<T>
        else -> Result.Failure(failMessage, 0)
    }
    is Result.Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is [T],
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is not [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Result<*>.filterIsInstance(failMessage: () -> String): Result<T> = when (val result = this) {
    is Result.Success -> when (result.item) {
        is T -> result as Result<T>
        else -> Result.Failure(failMessage(), 0)
    }
    is Result.Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is not null,
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is null.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T?>.filterNotNull(failMessage: String): Result<T> = when (this) {
    is Result.Success -> when (item) {
        null -> Result.Failure(failMessage, wordsTaken)
        else -> Result.Success(item, wordsTaken)
    }
    is Result.Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is not null,
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is null.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Any> Result<T?>.filterNotNull(failMessage: (T?) -> String): Result<T> = when (this) {
    is Result.Success -> when (item) {
        null -> Result.Failure(failMessage(item), wordsTaken)
        else -> Result.Success(item, wordsTaken)
    }
    is Result.Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the result of [filter] is a [FilterResult.Pass],
 * or a [Result.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.filter(filter: (T) -> FilterResult): Result<T> = when (this) {
    is Result.Success -> when (val result = filter(item)) {
        FilterResult.Pass -> this
        is FilterResult.Fail -> Result.Failure(result.reason, 0)
    }
    is Result.Failure -> this
}

/**
 * The result of a filter action, a [FilterResult] can either [Pass] or [Fail].
 */
sealed class FilterResult {

    /**
     * A successful filter attempt, the value that matched with this result will be propagated.
     */
    object Pass : FilterResult()

    /**
     * A failed filter attempt, supplying a [reason] for failure.
     */
    class Fail(val reason: String) : FilterResult() {
        companion object
    }

    companion object {
        inline operator fun invoke(predicate: Boolean, failureReason: () -> String) = when {
            predicate -> Pass
            else -> Fail(failureReason())
        }
    }
}

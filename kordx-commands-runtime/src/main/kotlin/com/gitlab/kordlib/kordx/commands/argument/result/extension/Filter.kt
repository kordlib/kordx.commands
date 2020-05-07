package com.gitlab.kordlib.kordx.commands.argument.result.extension

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult


/**
 * Returns a [ArgumentResult.Success] if this result is a [ArgumentResult.Success] *and* the [ArgumentResult.Success.item] is [T],
 * or a [ArgumentResult.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [ArgumentResult.Success.item] is not [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> ArgumentResult<*>.filterIsInstance(failMessage: String): ArgumentResult<T> = when (val result = this) {
    is ArgumentResult.Success -> when (result.item) {
        is T -> result as ArgumentResult<T>
        else -> ArgumentResult.Failure(failMessage, 0)
    }
    is ArgumentResult.Failure -> this as ArgumentResult<T>
}

/**
 * Returns a [ArgumentResult.Success] if this result is a [ArgumentResult.Success] *and* the [ArgumentResult.Success.item] is [T],
 * or a [ArgumentResult.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [ArgumentResult.Success.item] is not [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> ArgumentResult<*>.filterIsInstance(failMessage: () -> String): ArgumentResult<T> = when (val result = this) {
    is ArgumentResult.Success -> when (result.item) {
        is T -> result as ArgumentResult<T>
        else -> ArgumentResult.Failure(failMessage(), 0)
    }
    is ArgumentResult.Failure -> this as ArgumentResult<T>
}

/**
 * Returns a [ArgumentResult.Success] if this result is a [ArgumentResult.Success] *and* the [ArgumentResult.Success.item] is not null,
 * or a [ArgumentResult.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [ArgumentResult.Success.item] is null.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> ArgumentResult<T?>.filterNotNull(failMessage: String): ArgumentResult<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Failure(failMessage, wordsTaken)
        else -> ArgumentResult.Success(item, wordsTaken)
    }
    is ArgumentResult.Failure -> this as ArgumentResult<T>
}

/**
 * Returns a [ArgumentResult.Success] if this result is a [ArgumentResult.Success] *and* the [ArgumentResult.Success.item] is not null,
 * or a [ArgumentResult.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [ArgumentResult.Success.item] is null.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Any> ArgumentResult<T?>.filterNotNull(failMessage: (T?) -> String): ArgumentResult<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Failure(failMessage(item), wordsTaken)
        else -> ArgumentResult.Success(item, wordsTaken)
    }
    is ArgumentResult.Failure -> this as ArgumentResult<T>
}

/**
 * Returns a [ArgumentResult.Success] if this result is a [ArgumentResult.Success] *and* the result of [filter] is a [FilterResult.Pass],
 * or a [ArgumentResult.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> ArgumentResult<T>.filter(filter: (T) -> FilterResult): ArgumentResult<T> = when (this) {
    is ArgumentResult.Success -> when (val result = filter(item)) {
        FilterResult.Pass -> this
        is FilterResult.Fail -> ArgumentResult.Failure(result.reason, 0)
    }
    is ArgumentResult.Failure -> this
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

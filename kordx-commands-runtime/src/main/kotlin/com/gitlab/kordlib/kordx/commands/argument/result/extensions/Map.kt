package com.gitlab.kordlib.kordx.commands.argument.result.extensions

import com.gitlab.kordlib.kordx.commands.argument.result.Result


/**
 * Applies the [mapper] to the [Result.Success.item] if this is a [Result.Success] returning a [Result.Success] with
 * the new value, or a [Result.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(mapper(item), wordsTaken)
    is Result.Failure -> this as Result<R>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the result of [mapper] is a [MapResult.Pass],
 * or a [Result.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, R : Any> Result<T>.tryMap(mapper: (T) -> MapResult<R>): Result<R> = when (this) {
    is Result.Success -> when (val result = mapper(item)) {
        is MapResult.Pass -> Result.Success(result.item, wordsTaken)
        is MapResult.Fail -> Result.Failure(result.reason, 0)
    }
    is Result.Failure -> this as Result<R>
}

/**
 * The result of a mapping action, a [MapResult] can either [Pass] or [Fail].
 */
sealed class MapResult<T> {

    /**
     * A successful mapping attempt, the [item] will be propagated.
     */
    class Pass<T>(val item: T) : MapResult<T>() {
        companion object
    }

    /**
     * A failed mapping attempt, supplying a [reason] for failure.
     */
    class Fail<T>(val reason: String) : MapResult<T>() {
        companion object
    }

    companion object {

        inline fun <reified R> filterInstance(item: Any, failMessage: String): MapResult<R> = when (item) {
            is R -> Pass(item)
            else -> Fail(failMessage)
        }

        inline fun <reified R> filterInstance(item: Any, failMessage: () -> String): MapResult<R> = when (item) {
            is R -> Pass(item)
            else -> Fail(failMessage())
        }

    }
}

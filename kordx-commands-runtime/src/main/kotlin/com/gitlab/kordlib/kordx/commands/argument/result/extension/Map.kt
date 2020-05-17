package com.gitlab.kordlib.kordx.commands.argument.result.extension

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult


/**
 * Applies the [mapper] to the [ArgumentResult.Success.item] if  this is a [ArgumentResult.Success],
 * or a [ArgumentResult.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, R> ArgumentResult<T>.map(mapper: (T) -> R): ArgumentResult<R> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(mapper(item), wordsTaken)
    is ArgumentResult.Failure -> this as ArgumentResult<R>
}

/**
 * Returns a [ArgumentResult.Success] if
 * this result is a [ArgumentResult.Success] *and* the result of [mapper] is a [MapResult.Pass],
 * or a [ArgumentResult.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, R : Any> ArgumentResult<T>.tryMap(mapper: (T) -> MapResult<R>): ArgumentResult<R> = when (this) {
    is ArgumentResult.Success -> when (val result = mapper(item)) {
        is MapResult.Pass -> ArgumentResult.Success(result.item, wordsTaken)
        is MapResult.Fail -> ArgumentResult.Failure(result.reason, 0)
    }
    is ArgumentResult.Failure -> this as ArgumentResult<R>
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

    companion object
}

package com.gitlab.kordlib.kordx.commands.argument.result.extensions

import com.gitlab.kordlib.kordx.commands.argument.result.Result


/**
 * Returns the result of the [generator] if this is a [Result.Failure], or [this] otherwise.
 */
inline fun <T> Result<T>.switchOnFail(generator: () -> Result<T>): Result<T> = when (this) {
    is Result.Success -> this
    is Result.Failure -> generator()
}


/**
 * Returns this if this is a [Result.Success] and [Result.Success.item] is not null, or a [Result.Success] with
 * the [default] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T?>.orDefault(default: T): Result.Success<T> = when (this) {
    is Result.Success -> when (item) {
        null -> Result.Success(default, wordsTaken)
        else -> this as Result.Success<T>
    }
    is Result.Failure -> Result.Success(default, 0)
}

/**
 * Returns this if this is a [Result.Success] and [Result.Success.item] is not null, or a [Result.Success] with
 * the [fallback] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Any> Result<T?>.orSupplyDefault(fallback: () -> T): Result.Success<T> = when (this) {
    is Result.Success -> when (item) {
        null -> Result.Success(fallback(), wordsTaken)
        else -> this as Result.Success<T>
    }
    is Result.Failure -> Result.Success(fallback(), 0)
}


/**
 * Returns this if this is a [Result.Success], or a [Result.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Result<T>.orElse(fallBack: T): Result.Success<T> = when (this) {
    is Result.Success -> Result.Success(item, wordsTaken)
    is Result.Failure -> Result.Success(fallBack, 0)
}

/**
 * Returns this if this is a [Result.Success], or a [Result.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.orElseSupply(fallBack: () -> T): Result.Success<T> = when (this) {
    is Result.Success -> Result.Success(item, wordsTaken)
    is Result.Failure -> Result.Success(fallBack(), 0)
}

package com.gitlab.kordlib.kordx.commands.argument.result.extension

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult


/**
 * Returns the result of the [generator] if this is a [ArgumentResult.Failure], or [this] otherwise.
 */
inline fun <T> ArgumentResult<T>.switchOnFail(generator: () -> ArgumentResult<T>): ArgumentResult<T> = when (this) {
    is ArgumentResult.Success -> this
    is ArgumentResult.Failure -> generator()
}


/**
 * Returns this if this is a [ArgumentResult.Success] and [ArgumentResult.Success.item] is not null, or a [ArgumentResult.Success] with
 * the [default] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> ArgumentResult<T?>.orDefault(default: T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Success(default, wordsTaken)
        else -> this as ArgumentResult.Success<T>
    }
    is ArgumentResult.Failure -> ArgumentResult.Success(default, 0)
}

/**
 * Returns this if this is a [ArgumentResult.Success] and [ArgumentResult.Success.item] is not null, or a [ArgumentResult.Success] with
 * the [fallback] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
@JvmName("orElseSupplyNullable")
inline fun <T : Any> ArgumentResult<T?>.orElseSupply(fallback: () -> T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Success(fallback(), wordsTaken)
        else -> this as ArgumentResult.Success<T>
    }
    is ArgumentResult.Failure -> ArgumentResult.Success(fallback(), 0)
}


/**
 * Returns this if this is a [ArgumentResult.Success], or a [ArgumentResult.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T> ArgumentResult<T>.orElse(fallBack: T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(item, wordsTaken)
    is ArgumentResult.Failure -> ArgumentResult.Success(fallBack, 0)
}

/**
 * Returns this if this is a [ArgumentResult.Success], or a [ArgumentResult.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> ArgumentResult<T>.orElseSupply(fallBack: () -> T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(item, wordsTaken)
    is ArgumentResult.Failure -> ArgumentResult.Success(fallBack(), 0)
}

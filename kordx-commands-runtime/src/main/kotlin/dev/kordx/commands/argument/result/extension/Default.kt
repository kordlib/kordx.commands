package dev.kordx.commands.argument.result.extension

import dev.kordx.commands.argument.result.ArgumentResult


/**
 * Returns the result of the [generator] if this is a [ArgumentResult.Failure], or [this] otherwise.
 */
inline fun <T> ArgumentResult<T>.switchOnFail(generator: () -> ArgumentResult<T>): ArgumentResult<T> = when (this) {
    is ArgumentResult.Success -> this
    is ArgumentResult.Failure -> generator()
}


/**
 * Returns this if this is a [ArgumentResult.Success] and [ArgumentResult.Success.item] is not null,
 * or a [ArgumentResult.Success] with the [default] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> ArgumentResult<T?>.orDefault(default: T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Success(default, newIndex)
        else -> this as ArgumentResult.Success<T>
    }
    is ArgumentResult.Failure -> ArgumentResult.Success(default, atChar)
}

/**
 * Returns this if this is a [ArgumentResult.Success] and [ArgumentResult.Success.item] is not null,
 * or a [ArgumentResult.Success] with the [fallback] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
@JvmName("orElseSupplyNullable")
@Deprecated(
        "The behavior of this function is incorrect, use the variant with `failIndex` instead",
        ReplaceWith("orElseSupply(failIndex, fallback)"),
        level = DeprecationLevel.WARNING,
)
inline fun <T : Any> ArgumentResult<T?>.orElseSupply(fallback: () -> T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Success(fallback(), newIndex)
        else -> this as ArgumentResult.Success<T>
    }
    is ArgumentResult.Failure -> ArgumentResult.Success(fallback(), 0)
}

/**
 * Returns this if this is a [ArgumentResult.Success] and [ArgumentResult.Success.item] is not null,
 * or a [ArgumentResult.Success] with the [fallback] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
@JvmName("orElseSupplyNullable")
inline fun <T : Any> ArgumentResult<T?>.orElseSupply(
        failIndex: Int,
        fallback: () -> T
): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> when (item) {
        null -> ArgumentResult.Success(fallback(), newIndex)
        else -> this as ArgumentResult.Success<T>
    }
    is ArgumentResult.Failure -> ArgumentResult.Success(fallback(), failIndex)
}



/**
 * Returns this if this is a [ArgumentResult.Success],
 * or a [ArgumentResult.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
@Deprecated(
        "The behavior of this function is incorrect, use the variant with `failIndex` instead",
        ReplaceWith("orElse(failIndex, fallback)"),
        level = DeprecationLevel.WARNING,
)
fun <T> ArgumentResult<T>.orElse(fallBack: T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(item, newIndex)
    is ArgumentResult.Failure -> ArgumentResult.Success(fallBack, 0)
}

/**
 * Returns this if this is a [ArgumentResult.Success],
 * or a [ArgumentResult.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T> ArgumentResult<T>.orElse(failIndex: Int, fallBack: T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(item, newIndex)
    is ArgumentResult.Failure -> ArgumentResult.Success(fallBack, failIndex)
}

/**
 * Returns this if this is a [ArgumentResult.Success],
 * or a [ArgumentResult.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
@Deprecated(
        "The behavior of this function is incorrect, use the variant with `failIndex` instead",
        ReplaceWith("orElseSupply(failIndex, fallback)"),
        level = DeprecationLevel.WARNING,
)
inline fun <T> ArgumentResult<T>.orElseSupply(fallBack: () -> T): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(item, newIndex)
    is ArgumentResult.Failure -> ArgumentResult.Success(fallBack(), 0)
}

/**
 * Returns this if this is a [ArgumentResult.Success],
 * or a [ArgumentResult.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> ArgumentResult<T>.orElseSupply(
        failIndex: Int,
        fallBack: () -> T
): ArgumentResult.Success<T> = when (this) {
    is ArgumentResult.Success -> ArgumentResult.Success(item, newIndex)
    is ArgumentResult.Failure -> ArgumentResult.Success(fallBack(), failIndex)
}

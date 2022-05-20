package dev.kordx.commands.argument.result.extension

import dev.kordx.commands.argument.result.ArgumentResult

/**
 * Returns this if this is a [ArgumentResult.Success], or a [ArgumentResult.Success] with null as item otherwise.
 */
@Deprecated(
        "The behavior of this function is incorrect, use the variant with `failIndex` instead",
        ReplaceWith("optional(failIndex)"),
        level = DeprecationLevel.WARNING,
)
@Suppress("UNCHECKED_CAST")
fun <T : Any> ArgumentResult<T>.optional(): ArgumentResult.Success<T?> = when (this) {
    is ArgumentResult.Success -> this as ArgumentResult.Success<T?>
    is ArgumentResult.Failure -> ArgumentResult.Success(null, 0)
}

/**
 * Returns this if this is a [ArgumentResult.Success], or a [ArgumentResult.Success] with null as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> ArgumentResult<T>.optional(failIndex: Int): ArgumentResult.Success<T?> = when (this) {
    is ArgumentResult.Success -> this as ArgumentResult.Success<T?>
    is ArgumentResult.Failure -> ArgumentResult.Success(null, failIndex)
}

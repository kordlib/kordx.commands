package com.gitlab.kordlib.kordx.commands.argument.result.extension

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult

/**
 * Returns this if this is a [ArgumentResult.Success], or a [ArgumentResult.Success] with null as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> ArgumentResult<T>.optional(): ArgumentResult.Success<T?> = when (this) {
    is ArgumentResult.Success -> this as ArgumentResult.Success<T?>
    is ArgumentResult.Failure -> ArgumentResult.Success(null, 0)
}

package com.gitlab.kordlib.kordx.commands.argument.result.extension

import com.gitlab.kordlib.kordx.commands.argument.result.Result

/**
 * Returns this if this is a [Result.Success], or a [Result.Success] with null as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T>.optional(): Result.Success<T?> = when (this) {
    is Result.Success -> this as Result.Success<T?>
    is Result.Failure -> Result.Success(null, 0)
}

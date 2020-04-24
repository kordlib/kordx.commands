package com.gitlab.kordlib.kordx.commands.argument

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import kotlin.test.assertEquals

fun <T> Result<T>.requireSuccess(): Result.Success<T> {
    return if (this is Result.Success) this
    else error("expected result to be Success but was $this")
}

fun <T> Result<T>.requireFailure(): Result.Failure<T> {
    return if (this is Result.Failure) this
    else error("expected result to be Failure but was $this")
}

fun <T> Result<T>.requireOneTaken() = requireTaken(1)

fun <T> Result<T>.requireTaken(amount: Int) {
    val success = requireSuccess()
    assertEquals(amount, success.wordsTaken)
}

fun <T> Result<T>.requireItem(expected: T) {
    val success = requireSuccess()
    assertEquals(expected, success.item)
}

package dev.kordx.commands.argument

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import kotlin.test.assertEquals

fun <T> ArgumentResult<T>.requireSuccess(): ArgumentResult.Success<T> {
    return if (this is ArgumentResult.Success) this
    else error("expected result to be Success but was $this")
}

fun <T> ArgumentResult<T>.requireFailure(): ArgumentResult.Failure<T> {
    return if (this is ArgumentResult.Failure) this
    else error("expected result to be Failure but was $this")
}

fun <T> ArgumentResult<T>.requireOneTaken() = requireTaken(1)

fun <T> ArgumentResult<T>.requireTaken(amount: Int) {
    val success = requireSuccess()
    assertEquals(amount, success.newIndex)
}

fun <T> ArgumentResult<T>.requireItem(expected: T) {
    val success = requireSuccess()
    assertEquals(expected, success.item)
}

package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

/**
 * Argument that matches against a single world, emitting success when the word is a valid long value.
 */
open class LongArgument(override val name: String = "Number") : SingleWordArgument<Long, Any?>() {
    final override val example: String
        get() = Random.nextDouble(-100.0, 100.0).toString()

    final override suspend fun parse(word: String, context: Any?): Result<Long> = when (val number = word.toLongOrNull()) {
        null -> failure("Expected a number.")
        else -> success(number)
    }

    companion object : LongArgument()
}

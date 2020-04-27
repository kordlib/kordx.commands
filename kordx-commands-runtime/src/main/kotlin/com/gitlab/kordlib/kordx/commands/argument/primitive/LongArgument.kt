package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

internal class InternalLongArgument(override val name: String = "Number") : SingleWordArgument<Long, Any?>() {
    override val example: String
        get() = Random.nextDouble(-100.0, 100.0).toString()

    override suspend fun parse(word: String, context: Any?): Result<Long> = when (val number = word.toLongOrNull()) {
        null -> failure("Expected a number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid long value.
 */
val LongArgument: Argument<Long, Any?> = InternalLongArgument()

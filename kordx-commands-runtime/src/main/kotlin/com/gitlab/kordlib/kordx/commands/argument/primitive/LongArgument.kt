package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import kotlin.random.Random

internal class InternalLongArgument(override val name: String = "Number") : SingleWordArgument<Long, Any?>() {

    override suspend fun parse(
            word: String,
            context: Any?
    ): WordResult<Long> = when (val number = word.toLongOrNull()) {
        null -> failure("Expected a number.")
        else -> success(number)
    }

}

/**
 * Argument that matches against a single world, emitting success when the word is a valid long value.
 */
val LongArgument: Argument<Long, Any?> = InternalLongArgument()

/**
 * Argument with [name] that matches against a single world, emitting success when the word is a valid long value.
 */
@Suppress("FunctionName")
fun LongArgument(name: String): Argument<Long, Any?> = InternalLongArgument(name)

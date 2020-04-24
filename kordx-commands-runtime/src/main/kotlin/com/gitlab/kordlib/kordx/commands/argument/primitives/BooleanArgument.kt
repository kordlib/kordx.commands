package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import kotlin.random.Random

/**
 * Argument that parses a single word and compares it to [trueValue] (default "true") or [falseValue] (default "false"),
 * emitting `true` or `false` respectively and failing otherwise.
 *
 * @param ignoreCase true to ignore character case when comparing strings. By default true.
 */
open class BooleanArgument(
        override val name: String = "Boolean",
        private val trueValue: String = "true",
        private val falseValue: String = "false",
        private val ignoreCase: Boolean = true
) : SingleWordArgument<Boolean, Any?>() {

    init {
        require(" " !in trueValue) { "trueValue should not contain spaces but was '$trueValue'" }
        require(" " !in falseValue) { "falseValue should not contain spaces but was '$falseValue'" }
    }

    final override val example: String
        get() = Random.nextBoolean().toString()

    final override suspend fun parse(word: String, context: Any?): Result<Boolean> = when {
        word.equals(trueValue, ignoreCase) -> success(true)
        word.equals(falseValue, ignoreCase) -> success(false)
        else -> failure("Expected true or false.")
    }

    companion object : BooleanArgument()
}
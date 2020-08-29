package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import kotlin.random.Random

internal class InternalBooleanArgument(
        private val trueValue: String = "true",
        private val falseValue: String = "false",
        private val ignoreCase: Boolean = true
) : SingleWordArgument<Boolean, Any?>() {

    override val name: String = "Boolean"

    init {
        require(" " !in trueValue) { "trueValue should not contain spaces but was '$trueValue'" }
        require(" " !in falseValue) { "falseValue should not contain spaces but was '$falseValue'" }
    }

    override val example: String
        get() = Random.nextBoolean().toString()

    override suspend fun parse(word: String, context: Any?): WordResult<Boolean> = when {
        word.equals(trueValue, ignoreCase) -> success(true)
        word.equals(falseValue, ignoreCase) -> success(false)
        else -> failure("Expected true or false.")
    }

}
/**
 * Argument that parses a single word and compares it to "true" or "false" ignoring case,
 * emitting `true` or `false` respectively and failing otherwise.
 */
val BooleanArgument: Argument<Boolean, Any?> = InternalBooleanArgument()

/**
 * Argument that parses a single word and compares it to [trueValue] (default "true") or [falseValue] (default "false"),
 * emitting `true` or `false` respectively and failing otherwise.
 *
 * @param trueValue the value that, when matched emits `true`, default "true"
 * @param falseValue the value that, when matched emits `false`, default "false"
 * @param ignoreCase true to ignore character case when comparing strings. By default true.
 */
@Suppress("FunctionName")
fun BooleanArgument(
        trueValue: String = "true",
        falseValue: String = "false",
        ignoreCase: Boolean = true
): Argument<Boolean, Any?> = InternalBooleanArgument(trueValue, falseValue, ignoreCase)

package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.FilterResult
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.filter

/**
 * Returns an Argument that, on top of the supplied argument, only accepts values in [whitelist].
 *
 * @param ignoreCase true to ignore character case when comparing strings. By default true.
 */
fun <CONTEXT> Argument<String, CONTEXT>.whitelist(
        vararg whitelist: String, ignoreCase: Boolean = true
): Argument<String, CONTEXT> = object : Argument<String, CONTEXT> by this {
    override val example: String
        get() = whitelist.random()

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<String> {
        return this@whitelist.parse(words, fromIndex, context).filter {
            when {
                ignoreCase -> when {
                    whitelist.any { word -> word.equals(it, true) } -> FilterResult.Pass
                    else -> FilterResult.Fail("expected one of $words (not case sensitive) but got $it")
                }
                it in whitelist -> FilterResult.Pass
                else -> FilterResult.Fail("expected one of $words (case sensitive) but got $it")
            }
        }
    }
}
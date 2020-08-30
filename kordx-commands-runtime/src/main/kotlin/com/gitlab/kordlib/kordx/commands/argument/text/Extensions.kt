package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.FilterResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.filter

/**
 * Returns an Argument that, on top of the supplied argument, only accepts values in [whitelist].
 *
 * @param ignoreCase true to ignore character case when comparing strings. By default true.
 */
fun <CONTEXT> Argument<String, CONTEXT>.whitelist(
        vararg whitelist: String, ignoreCase: Boolean = true
): Argument<String, CONTEXT> = object : Argument<String, CONTEXT> by this {
    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<String> {
        return this@whitelist.parse(text, fromIndex, context).filter {
            when {
                ignoreCase -> when {
                    whitelist.any { word -> word.equals(it, true) } -> FilterResult.Pass
                    else -> FilterResult.Fail("expected one of $whitelist (not case sensitive) but got $it")
                }
                it in whitelist -> FilterResult.Pass
                else -> FilterResult.Fail("expected one of $whitelist (case sensitive) but got $it")
            }
        }
    }
}

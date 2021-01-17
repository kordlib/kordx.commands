package dev.kord.x.commands.argument.text

import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.result.ArgumentResult
import dev.kord.x.commands.argument.result.extension.FilterResult
import dev.kord.x.commands.argument.result.extension.filter
import java.util.*

/**
 * Returns an Argument that, on top of the supplied argument, only accepts values in [whitelist].
 *
 * @param ignoreCase true to ignore character case when comparing strings. By default true.
 */
fun <CONTEXT> Argument<String, CONTEXT>.whitelist(
    vararg whitelist: String, ignoreCase: Boolean = true
): Argument<String, CONTEXT> = object : Argument<String, CONTEXT> by this {
    private val options = whitelist.contentToString()
    override suspend fun parse(
        text: String,
        fromIndex: Int,
        context: CONTEXT
    ): ArgumentResult<String> {
        return this@whitelist.parse(text, fromIndex, context).filter(fromIndex) {
            when {
                ignoreCase -> when {
                    whitelist.any { word -> word.equals(it, true) } -> FilterResult.Pass
                    else -> FilterResult.Fail("expected one of $options (not case sensitive) but got $it")
                }
                it in whitelist -> FilterResult.Pass
                else -> FilterResult.Fail("expected one of $options (case sensitive) but got $it")
            }
        }
    }
}

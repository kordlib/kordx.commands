package dev.kord.x.commands.argument.text

import dev.kord.x.commands.argument.result.ArgumentResult
import dev.kord.x.commands.argument.state.*

internal class InternalQuotedArgument(
    override val name: String,
    private val prefix: String,
    private val suffix: String
) : StateArgument<String, Any?>() {

    override suspend fun ParseState.parse(context: Any?): ArgumentResult<String> {
        if (ended) return unexpectedEnd()

        if (!drop(prefix)) {
            return expected(prefix)
        }

        consumeUntil { remaining.startsWith(suffix) }

        if (!drop(suffix)) {
            return expected(suffix)
        }

        return success()
    }

}

/**
 * Argument that accepts any String prefixed by [prefix] (default `"`) and suffixed by [suffix] (default [prefix]).
 */
@Suppress("FunctionName")
fun QuotedArgument(
    name: String = "Quoted Text",
    prefix: String = "\"",
    suffix: String = prefix
): dev.kord.x.commands.argument.Argument<String, Any?> =
    InternalQuotedArgument(name, prefix, suffix)

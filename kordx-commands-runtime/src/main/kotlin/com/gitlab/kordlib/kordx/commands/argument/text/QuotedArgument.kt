package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.state.*

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

        if (!drop(suffix)){
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
): Argument<String, Any?> = InternalQuotedArgument(name, prefix, suffix)

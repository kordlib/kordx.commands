package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument
import com.gitlab.kordlib.kordx.commands.argument.state.*

internal class InternalStringArgument(override val name: String = "Text") : StateArgument<String, Any?>() {

    override suspend fun ParseState.parse(context: Any?): ArgumentResult<String> {
        if (ended) return unexpectedEnd()
        consumeAll()
        return success()
    }

}

/**
 * Argument that accepts any String, returning the matched words joined to a single string,
 * separating the words with a space.
 *
 * > This Argument will consume all remaining words
 */
val StringArgument: Argument<String, Any?> = InternalStringArgument()

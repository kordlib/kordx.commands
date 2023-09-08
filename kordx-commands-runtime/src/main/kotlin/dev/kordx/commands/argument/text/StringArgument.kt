package dev.kordx.commands.argument.text

import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.state.*

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
val StringArgument: dev.kordx.commands.argument.Argument<String, Any?> = InternalStringArgument()

package dev.kord.x.commands.argument.text

import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

internal class InternalWordArgument(override val name: String = "Word") :
    SingleWordArgument<String, Any?>() {
    override suspend fun parse(word: String, context: Any?): WordResult<String> = success(word)
}

/**
 * Argument that accepts any single word, returning the matched word.
 */
val WordArgument: dev.kord.x.commands.argument.Argument<String, Any?> = InternalWordArgument()

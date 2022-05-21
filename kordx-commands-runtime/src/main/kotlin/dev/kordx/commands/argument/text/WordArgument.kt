package dev.kordx.commands.argument.text

import dev.kordx.commands.argument.result.WordResult

internal class InternalWordArgument(override val name: String = "Word") : dev.kordx.commands.argument.SingleWordArgument<String, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<String> = success(word)
}

/**
 * Argument that accepts any single word, returning the matched word.
 */
val WordArgument: dev.kordx.commands.argument.Argument<String, Any?> = InternalWordArgument()

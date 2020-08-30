package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import com.gitlab.kordlib.kordx.commands.argument.state.*

internal class InternalWordArgument(override val name: String = "Word") : SingleWordArgument<String, Any?>() {

    override suspend fun parse(word: String, context: Any?): WordResult<String> = success(word)
}

/**
 * Argument that accepts any single word, returning the matched word.
 */
val WordArgument: Argument<String, Any?> = InternalWordArgument()

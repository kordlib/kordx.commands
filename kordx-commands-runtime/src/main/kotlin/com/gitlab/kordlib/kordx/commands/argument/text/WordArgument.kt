package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

internal class InternalWordArgument(override val name: String = "Word") : SingleWordArgument<String, Any?>() {

    override val example: String
        get() = listOf("epeolatry", "functionalism", "koan").random()

    override suspend fun parse(word: String, context: Any?): Result<String> = success(word)
}

/**
 * Argument that accepts any single word, returning the matched word.
 */
val WordArgument: Argument<String, Any?> = InternalWordArgument()

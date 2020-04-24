package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

/**
 * Argument that accepts any single word, returning the matched word.
 */
open class WordArgument(override val name: String = "Word") : SingleWordArgument<String, Any?>() {

    final override val example: String
        get() = listOf("epeolatry", "functionalism", "koan").random()

    final override suspend fun parse(word: String, context: Any?): Result<String> = success(word)

    companion object : WordArgument()
}
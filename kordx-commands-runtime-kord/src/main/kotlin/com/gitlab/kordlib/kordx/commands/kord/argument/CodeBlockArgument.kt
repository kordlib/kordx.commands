package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.state.*
import java.util.*

//intentionally not a data class for binary compatibility reasons
/**
 * A Discord code block containing an optional [language] and a mandatory [content].
 */
@Suppress("MemberVisibilityCanBePrivate")
class CodeBlock(val language: String?, val content: String) {

    override fun equals(other: Any?): Boolean {
        val block = other as? CodeBlock ?: return false
        return block.language == language && block.content == content
    }

    override fun hashCode(): Int = Objects.hash(language, content)

    override fun toString(): String = "CodeBlock(language=$language, content=$content)"

}

private const val CODE_BLOCK_QUOTES = "```"

internal object InternalCodeBlockArgument : StateArgument<CodeBlock, Any?>() {

    override val name: String
        get() = "Code Block"

    override suspend fun ParseState.parse(context: Any?): ArgumentResult<CodeBlock> {
        if (ended) return unexpectedEnd()

        if (!drop(CODE_BLOCK_QUOTES)) {
            return expected(CODE_BLOCK_QUOTES)
        }

        val maybeLanguage = flush { consumeUntil { it == '\n' || remaining.startsWith(CODE_BLOCK_QUOTES) } }
        val newline = drop("\n")

        var content = flush { consumeUntil { remaining.startsWith(CODE_BLOCK_QUOTES) } }

        if(!drop(CODE_BLOCK_QUOTES)) {
            return expected(CODE_BLOCK_QUOTES)
        }

        val language = when {
            maybeLanguage.contains(" ") -> {
                content = maybeLanguage + (if (newline) "\n" else "") + content
                null
            }
            content.isBlank() -> {
                content = maybeLanguage + (if (newline) "\n" else "") + content
                null
            }
            else -> maybeLanguage
        }

        return success(CodeBlock(language, content))
    }

}

/**
 * An argument that matches against any Discord code block.
 *
 * Note that there are some edge cases when resolving a code block's language, invalid language declarations
 * will be added the [CodeBlock.content] instead.
 * 
 * A valid language includes a set of non-space characters between the code block start and the first newline:
 * ```
 * ´´´kotlin//valid
 * println("hello world")
 * ´´´
 * ```
 * A language prefixed or suffixed by a space is not valid:
 *```
 * ´´´ kotlin //not valid
 * println("hello world")
 * ´´´
 * ```
 * A language without content or newline is not valid:
 * ```
 * ´´´kotlin´´´ //not valid
 * ```
 */
val CodeBlockArgument: Argument<CodeBlock, Any?> = InternalCodeBlockArgument

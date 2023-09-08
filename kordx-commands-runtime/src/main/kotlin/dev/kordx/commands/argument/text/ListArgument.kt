package dev.kordx.commands.argument.text

import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.state.*

internal class InternalListArgument(
        private val separator: String = "|"
) : StateArgument<List<String>, Any?>() {
    override val name: String = "Separated $separator text"

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun ParseState.parse(context: Any?): ArgumentResult<List<String>> {
        if (ended) return unexpectedEnd()

        @Suppress("RemoveExplicitTypeArguments")
        val list = buildList<String> {
            while (!ended) {
                add(flush { consumeUntil { remaining.startsWith(separator) } })
                drop(separator)
            }
        }

        return success(list)
    }

}

/**
 * Argument that accepts any String, returning the matched words split by "|" as opposed to whitespaces.
 *
 * > This Argument will consume all remaining words
 */
val ListArgument: dev.kordx.commands.argument.Argument<List<String>, Any?> = InternalListArgument()

/**
 * Argument that accepts any String, returning the matched words split by the [separator] as opposed to whitespaces.
 *
 * @param separator the lists's separator. `"|"` by default.
 *
 * > This Argument will consume all remaining words
 */
@Suppress("FunctionName")
fun ListArgument(separator: String = "|"): dev.kordx.commands.argument.Argument<List<String>, Any?> = InternalListArgument(separator)

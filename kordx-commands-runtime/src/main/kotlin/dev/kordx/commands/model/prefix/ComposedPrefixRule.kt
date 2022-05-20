package dev.kordx.commands.model.prefix

/**
 * Combines both prefixes into a new prefix. Supplying the [PrefixRule.Result] of the [other] rule if the first one
 * returns [PrefixRule.Result.Denied].
 */
infix fun <T> PrefixRule<T>.or(other: PrefixRule<T>) : PrefixRule<T> = ComposedPrefixRule(this, other)

internal class ComposedPrefixRule<in T>(
        private val first: PrefixRule<T>,
        private val second: PrefixRule<T>
) : PrefixRule<T>{
    override suspend fun consume(message: String, context: T): PrefixRule.Result {
        return first.consume(message, context).switchOnFail { second.consume(message, context) }
    }
}


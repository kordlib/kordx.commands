package dev.kord.x.commands.model.prefix

/**
 * Creates a [PrefixRule] that accepts messages starting with the [prefix].
 */
fun PrefixBuilder.literal(prefix: String): PrefixRule<Any?> = LiteralPrefixRule(prefix)

internal class LiteralPrefixRule(private val prefix: String) : PrefixRule<Any?> {

    override suspend fun consume(message: String, context: Any?): PrefixRule.Result = when {
        message.startsWith(prefix) -> PrefixRule.Result.Accepted(prefix)
        else -> PrefixRule.Result.Denied
    }

}

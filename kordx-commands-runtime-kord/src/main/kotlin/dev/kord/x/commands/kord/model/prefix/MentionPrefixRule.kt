package dev.kord.x.commands.kord.model.prefix

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.model.prefix.PrefixBuilder
import dev.kord.x.commands.model.prefix.PrefixRule
import org.koin.core.get

/**
 * Creates [PrefixRule] that accepts the bot's mention as a valid prefix.
 *
 * > Note that due to the nature of Discord's mentions requiring a space for them to be rendered in the client,
 * this rule will also require and consume a whitespace character directly after the mention.
 * If, for example, this rule is enabled and the given input is `"@Bot ping"` the matched prefix will be `"@Bot "`,
 * including the space. This also means that simply mentioning the bot `"@Bot"` will not be considered valid.
 */
fun PrefixBuilder.mention(): PrefixRule<MessageCreateEvent> = MentionPrefixRule(get())

internal class MentionPrefixRule(kord: Kord) : PrefixRule<MessageCreateEvent> {

    private val regex = Regex("""<@!?${kord.selfId}>\s""")

    override suspend fun consume(message: String, context: MessageCreateEvent): PrefixRule.Result {
        val result = regex.find(message) ?: return PrefixRule.Result.Denied
        if (result.range.first != 0) return PrefixRule.Result.Denied
        return PrefixRule.Result.Accepted(message.substring(result.range))
    }

}

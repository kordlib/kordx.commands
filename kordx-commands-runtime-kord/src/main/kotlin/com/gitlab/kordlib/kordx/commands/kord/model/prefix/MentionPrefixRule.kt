package com.gitlab.kordlib.kordx.commands.kord.model.prefix

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixRule
import org.koin.core.get

/**
 * Creates [PrefixRule] that accepts the bot's mention as a valid prefix.
 *
 * > Note that due to the nature of Discord's mentions requiring a space for them to be rendered in the client,
 * this rule will also require and consume a whitespace character directly after the mention.
 * If, for example, this rule is enabled and the given input is `"@Bot ping"` the matched prefix will be `"@Bot "`,
 * including the space. This also means that simply mentioning the bot `"@Bot"` will not be considered valid.
 */
fun PrefixBuilder.mention(): PrefixRule<KordEventAdapter> = MentionPrefixRule(get())

internal class MentionPrefixRule(kord: Kord) : PrefixRule<KordEventAdapter> {

    private val regex = Regex("""<@!?${kord.selfId.value}>\s""")

    override suspend fun consume(message: String, context: KordEventAdapter): PrefixRule.Result {
        val result = regex.find(message) ?: return PrefixRule.Result.Denied
        if (result.range.first != 0) return PrefixRule.Result.Denied
        return PrefixRule.Result.Accepted(message.substring(result.range))
    }

}

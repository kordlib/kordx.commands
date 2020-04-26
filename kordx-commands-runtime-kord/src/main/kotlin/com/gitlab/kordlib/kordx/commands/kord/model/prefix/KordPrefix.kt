package com.gitlab.kordlib.kordx.commands.kord.model.prefix

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.event.message.MessageCreateEvent

internal fun mentionPrefix(botId: Snowflake, supplier: (suspend (MessageCreateEvent) -> String)?): suspend (MessageCreateEvent) -> String {
    if (supplier == null) return onlyBotMention(botId)

    val regex = Regex("""^<(@|@!)${botId.value}>$""")
    return {
        val words = it.message.content.split(" ")
        when (words.firstOrNull()?.matches(regex)) {
            true -> words.first() + " "
            else -> supplier(it)
        }
    }
}

private fun onlyBotMention(botId: Snowflake): suspend (MessageCreateEvent) -> String {
    val regex = Regex("""^<(@|@!)${botId.value}>$""")
    return {
        val words = it.message.content.split(" ")
        if (words.firstOrNull()?.matches(regex) == true) words.first()
        else it.kord.unsafe.user(it.kord.selfId).mention
    }
}
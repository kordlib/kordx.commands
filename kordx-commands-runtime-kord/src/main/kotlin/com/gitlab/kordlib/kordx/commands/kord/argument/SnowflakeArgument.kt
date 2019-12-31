package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<(@|@&|@!|a:\w+:|:\w+:|#)\d+>$""")

open class SnowflakeArgument(override val name: String = "discord mention") : SingleWordArgument<Snowflake, MessageCreateEvent>() {
    final override val example: String
        get() = listOf("#channel", "@user", ":customEmoji:").random()

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : SnowflakeArgument()

}
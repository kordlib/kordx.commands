package com.gitlab.kordlib.kordx.commands.argument.discord

import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.kordx.commands.argument.ParsingContext
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<(@|@&|@!|a:\w+:|:\w+:|#)\d+>$""")

open class SnowflakeArgument(override val name: String = "discord mention") : SingleWordArgument<Snowflake>() {
    final override val example: String
        get() = listOf("#channel", "@user", ":customEmoji:").random()

    final override suspend fun parse(word: String, context: ParsingContext): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected discord mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : SnowflakeArgument()
}
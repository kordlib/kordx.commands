package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.core.entity.channel.Channel
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<#\d+>$""")

open class ChannelSnowflakeArgument(override val name: String = "Channel") : SingleWordArgument<Snowflake, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected discord mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : ChannelSnowflakeArgument()

}

open class ChannelArgument(override val name: String = "Channel")  : SingleWordArgument<Channel, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Channel> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected discord mention.")
        }

        return when (val channel = context.kord.getChannel(snowflake)) {
            null -> failure("Channel not found.")
            else -> success(channel)
        }
    }

    companion object : ChannelArgument()

}


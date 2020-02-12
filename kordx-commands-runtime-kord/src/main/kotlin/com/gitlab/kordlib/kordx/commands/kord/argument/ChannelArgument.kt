package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.channel.Channel
import com.gitlab.kordlib.core.entity.channel.MessageChannel
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.entity.channel.VoiceChannel
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<#\d+>$""")

open class ChannelSnowflakeArgument(override val name: String = "Channel") : SingleWordArgument<Snowflake, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : ChannelSnowflakeArgument()

}

open class ChannelArgument(override val name: String = "Channel") : SingleWordArgument<Channel, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Channel> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val channel = context.kord.getChannel(snowflake)) {
            null -> failure("Channel not found.")
            else -> success(channel)
        }
    }

    companion object : ChannelArgument()

}

open class TextChannelArgument(override val name: String = "Guild text channel") : SingleWordArgument<TextChannel, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<TextChannel> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val channel = context.kord.getChannel(snowflake)) {
            null -> failure("Channel not found.")
            is TextChannel -> success(channel)
            else -> failure("Expected guild text channel.")
        }
    }

    companion object : TextChannelArgument()

}

open class MessageChannelArgument(override val name: String = "Guild text channel") : SingleWordArgument<MessageChannel, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<MessageChannel> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val channel = context.kord.getChannel(snowflake)) {
            null -> failure("Channel not found.")
            is MessageChannel -> success(channel)
            else -> failure("Expected guild text channel.")
        }
    }

    companion object : MessageChannelArgument()

}

open class VoiceChannelArgument(override val name: String = "Guild text channel") : SingleWordArgument<VoiceChannel, MessageCreateEvent>() {

    final override val example: String
        get() = "#Channel"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<VoiceChannel> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val channel = context.kord.getChannel(snowflake)) {
            null -> failure("Channel not found.")
            is VoiceChannel -> success(channel)
            else -> failure("Expected guild text channel.")
        }
    }

    companion object : VoiceChannelArgument()

}
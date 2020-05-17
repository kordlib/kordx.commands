package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.channel.Channel
import com.gitlab.kordlib.core.entity.channel.MessageChannel
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.entity.channel.VoiceChannel
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.extension.filterIsInstance
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult

private val mentionRegex = Regex("""^<#\d+>$""")

internal class InternalChannelArgument(
        override val name: String = "Channel"
) : SingleWordArgument<Channel, MessageCreateEvent>() {

    override val example: String
        get() = "#Channel"

    override suspend fun parse(word: String, context: MessageCreateEvent): ArgumentResult<Channel> {
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
}

/**
 * Argument that matches against a channel mention or a channel id as a number.
 */
val ChannelArgument: Argument<Channel, MessageCreateEvent> = InternalChannelArgument()

/**
 * Argument that matches against a channel mention or a channel id as a number.
 *
 * @param name The name of the argument.
 */
@Suppress("FunctionName")
fun ChannelArgument(name: String): Argument<Channel, MessageCreateEvent> = InternalChannelArgument(name)


/**
 * Argument that matches against a TextChannel mention or a TextChannel id as a number.
 */
val TextChannelArgument = TextChannelArgument("Guild text channel")

/**
 * Argument that matches against a TextChannel mention or a TextChannel id as a number.
 *
 * @param name The name of the argument.
 */
@Suppress("FunctionName")
fun TextChannelArgument(name: String): Argument<TextChannel, MessageCreateEvent> = InternalChannelArgument(name)
        .filterIsInstance("Expected guild text channel.")


/**
 * Argument that matches against a MessageChannel mention or a MessageChannel id as a number.
 */
val MessageChannelArgument = MessageChannelArgument("Text channel")

/**
 * Argument that matches against a MessageChannel mention or a MessageChannel id as a number.
 *
 * @param name The name of the argument.
 */
@Suppress("FunctionName")
fun MessageChannelArgument(name: String): Argument<MessageChannel, MessageCreateEvent> = InternalChannelArgument(name)
        .filterIsInstance("Expected text channel.")

/**
 * Argument that matches against a VoiceChannel mention or a VoiceChannel id as a number.
 */
val VoiceChannelArgument = VoiceChannelArgument("Guild voice channel")


/**
 * Argument that matches against a VoiceChannel mention or a VoiceChannel id as a number.
 *
 * @param name The name of the argument.
 */
@Suppress("FunctionName")
fun VoiceChannelArgument(name: String): Argument<VoiceChannel, MessageCreateEvent> = InternalChannelArgument(name)
        .filterIsInstance("Expected a voice channel")

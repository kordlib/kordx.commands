package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.channel.Channel
import com.gitlab.kordlib.core.entity.channel.MessageChannel
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.entity.channel.VoiceChannel
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.extension.filterIsInstance
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter

private val mentionRegex = Regex("""^<#\d+>$""")

internal class InternalChannelArgument(
        override val name: String = "Channel"
) : SingleWordArgument<Channel, KordEventAdapter>() {

    override suspend fun parse(word: String, context: KordEventAdapter): WordResult<Channel> {
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
val ChannelArgument: Argument<Channel, KordEventAdapter> = InternalChannelArgument()

/**
 * Argument that matches against a channel mention or a channel id as a number.
 *
 * @param name The name of the argument.
 */
@Suppress("FunctionName")
fun ChannelArgument(name: String): Argument<Channel, KordEventAdapter> = InternalChannelArgument(name)


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
fun TextChannelArgument(name: String): Argument<TextChannel, KordEventAdapter> = InternalChannelArgument(name)
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
fun MessageChannelArgument(name: String): Argument<MessageChannel, KordEventAdapter> = InternalChannelArgument(name)
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
fun VoiceChannelArgument(name: String): Argument<VoiceChannel, KordEventAdapter> = InternalChannelArgument(name)
        .filterIsInstance("Expected a voice channel")

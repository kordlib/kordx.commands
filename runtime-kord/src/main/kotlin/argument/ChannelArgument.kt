package dev.kord.x.commands.kord.argument

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.extension.filterIsInstance
import dev.kord.x.commands.argument.result.WordResult

private val mentionRegex = Regex("""^<#\d+>$""")

internal class InternalChannelArgument(
    override val name: String = "Channel"
) : dev.kord.x.commands.argument.SingleWordArgument<Channel, MessageCreateEvent>() {

    override suspend fun parse(word: String, context: MessageCreateEvent): WordResult<Channel> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(
                word.removeSuffix(">").dropWhile { !it.isDigit() })
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
val ChannelArgument: Argument<Channel, MessageCreateEvent> =
    InternalChannelArgument()

/**
 * Argument that matches against a channel mention or a channel id as a number.
 *
 * @param name The name of the argument.
 */
@Suppress("FunctionName")
fun ChannelArgument(name: String): Argument<Channel, MessageCreateEvent> =
    InternalChannelArgument(name)


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
fun TextChannelArgument(name: String): Argument<TextChannel, MessageCreateEvent> =
    InternalChannelArgument(name)
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
fun MessageChannelArgument(name: String): Argument<MessageChannel, MessageCreateEvent> =
    InternalChannelArgument(name)
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
fun VoiceChannelArgument(name: String): Argument<VoiceChannel, MessageCreateEvent> =
    InternalChannelArgument(name)
        .filterIsInstance("Expected a voice channel")

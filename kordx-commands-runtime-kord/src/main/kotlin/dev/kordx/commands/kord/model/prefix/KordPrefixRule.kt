package dev.kordx.commands.kord.model.prefix

import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.MemberBehavior
import dev.kord.core.behavior.UserBehavior
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.entity.channel.Channel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.model.prefix.PrefixBuilder
import dev.kordx.commands.model.prefix.PrefixRule

/**
 * Creates a [PrefixRule] that supplies][supplier] a prefix based on the [event's][MessageCreateEvent] guild.
 *
 * Events created outside of guilds (DMs) will automatically be denied. Invocations of the [supplier] that return
 * `null` will be considered as denied as well.
 */
fun PrefixBuilder.guild(
        supplier: (guild: GuildBehavior) -> String?
): PrefixRule<MessageCreateEvent> = PrefixRule { message, context ->
    val guild = context.message.getGuildOrNull() ?: return@PrefixRule PrefixRule.Result.Denied
    val prefix = supplier(guild) ?: return@PrefixRule PrefixRule.Result.Denied
    if (message.startsWith(prefix)) PrefixRule.Result.Accepted(prefix)
    else PrefixRule.Result.Denied
}

/**
 * Creates a [PrefixRule] that supplies][supplier] a prefix based on the [event's][MessageCreateEvent] channel.
 *
 * Invocations of the [supplier] that return `null` will be considered as [denied][PrefixRule.Result.Denied].
 */
fun PrefixBuilder.channel(
        supplier: (channel: MessageChannelBehavior) -> String?
): PrefixRule<MessageCreateEvent> = PrefixRule { message, context ->
    val channel = context.message.channel
    val prefix = supplier(channel) ?: return@PrefixRule PrefixRule.Result.Denied
    if (message.startsWith(prefix)) PrefixRule.Result.Accepted(prefix)
    else PrefixRule.Result.Denied
}

/**
 * Creates a [PrefixRule] that supplies][supplier] a prefix based on the [event's][MessageCreateEvent] channel.
 *
 * Events created outside of channels of type [T] will automatically be [denied][PrefixRule.Result.Denied].
 * Invocations of the [supplier] that return `null` will be considered as [denied][PrefixRule.Result.Denied] as well.
 */
@JvmName("channelReified")
inline fun <reified T : Channel> PrefixBuilder.channel(
        noinline supplier: (channel: T) -> String?
): PrefixRule<MessageCreateEvent> = PrefixRule { message, context ->
    val channel = context.message.getChannel()
    if (channel !is T) return@PrefixRule PrefixRule.Result.Denied
    val prefix = supplier(channel) ?: return@PrefixRule PrefixRule.Result.Denied
    if (message.startsWith(prefix)) PrefixRule.Result.Accepted(prefix)
    else PrefixRule.Result.Denied
}

/**
 * Creates a [PrefixRule] that supplies][supplier] a prefix based on the [event's][MessageCreateEvent] user.
 *
 * Invocations of the [supplier] that return `null` will be considered as [denied][PrefixRule.Result.Denied].
 */
fun PrefixBuilder.user(
        supplier: (user: UserBehavior) -> String?
): PrefixRule<MessageCreateEvent> = PrefixRule { message, context ->
    val user = context.message.author ?: return@PrefixRule PrefixRule.Result.Denied
    val prefix = supplier(user) ?: return@PrefixRule PrefixRule.Result.Denied
    if (message.startsWith(prefix)) PrefixRule.Result.Accepted(prefix)
    else PrefixRule.Result.Denied
}

/**
 * Creates a [PrefixRule] that supplies][supplier] a prefix based on the [event's][MessageCreateEvent] member.
 *
 * Invocations of the [supplier] that return `null` will be considered as [denied][PrefixRule.Result.Denied].
 */
fun PrefixBuilder.member(
        supplier: (user: MemberBehavior) -> String?
): PrefixRule<MessageCreateEvent> = PrefixRule { message, context ->
    val member = context.message.getAuthorAsMember() ?: return@PrefixRule PrefixRule.Result.Denied
    val prefix = supplier(member) ?: return@PrefixRule PrefixRule.Result.Denied
    if (message.startsWith(prefix)) PrefixRule.Result.Accepted(prefix)
    else PrefixRule.Result.Denied
}

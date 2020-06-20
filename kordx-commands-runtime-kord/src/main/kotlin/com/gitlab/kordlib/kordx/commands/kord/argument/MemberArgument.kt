package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

internal class InternalMemberArgument(
        override val name: String = "User"
) : SingleWordArgument<Member, MessageCreateEvent>() {
    override val example: String
        get() = "@User"

    override suspend fun parse(word: String, context: MessageCreateEvent): ArgumentResult<Member> {
        val guildId = context.getGuild()?.id ?: return failure("Can't get members outside of guilds.")

        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val member = context.kord.getGuild(guildId)?.getMember(snowflake)) {
            null -> failure("User not found.")
            else -> success(member)
        }
    }

}

/**
 * Argument that matches against a user/member mention in a guild channel or a user id as a number.
 */
val MemberArgument: Argument<Member, MessageCreateEvent> = InternalMemberArgument()

/**
 * Argument that matches against a user/member mention in a guild channel or a user id as a number.
 *
 * @param name The name of this argument.
 */
@Suppress("FunctionName")
fun MemberArgument(name: String): Argument<Member, MessageCreateEvent> = InternalMemberArgument(name)

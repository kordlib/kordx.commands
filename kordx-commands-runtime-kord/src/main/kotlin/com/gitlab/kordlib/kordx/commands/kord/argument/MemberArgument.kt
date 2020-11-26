package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument
import com.gitlab.kordlib.kordx.commands.argument.result.WordResult
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

internal class InternalMemberArgument(
        override val name: String = "User"
) : SingleWordArgument<Member, KordEventAdapter>() {

    override suspend fun parse(word: String, context: KordEventAdapter): WordResult<Member> {
        val guildId = context.guild?.id ?: return failure("Can't get members outside of guilds.")

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
val MemberArgument: Argument<Member, KordEventAdapter> = InternalMemberArgument()

/**
 * Argument that matches against a user/member mention in a guild channel or a user id as a number.
 *
 * @param name The name of this argument.
 */
@Suppress("FunctionName")
fun MemberArgument(name: String): Argument<Member, KordEventAdapter> = InternalMemberArgument(name)

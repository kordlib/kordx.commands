package dev.kord.x.commands.kord.argument

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

internal class InternalMemberArgument(
    override val name: String = "User"
) : SingleWordArgument<Member, MessageCreateEvent>() {

    override suspend fun parse(word: String, context: MessageCreateEvent): WordResult<Member> {
        val guildId =
            context.getGuild()?.id ?: return failure("Can't get members outside of guilds.")

        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(
                word.removeSuffix(">").dropWhile { !it.isDigit() })
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
val MemberArgument: Argument<Member, MessageCreateEvent> =
    InternalMemberArgument()

/**
 * Argument that matches against a user/member mention in a guild channel or a user id as a number.
 *
 * @param name The name of this argument.
 */
@Suppress("FunctionName")
fun MemberArgument(name: String): Argument<Member, MessageCreateEvent> =
    InternalMemberArgument(name)

package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

open class MemberSnowflakeArgument(override val name: String = "User") : SingleWordArgument<Snowflake, MessageCreateEvent>() {

    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Snowflake> {
        context.message.guildId ?: return failure("Can't get member outside of guilds.")

        if (!word.matches(mentionRegex)) return failure("Expected discord mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : MemberSnowflakeArgument()
}


open class MemberArgument(override val name: String = "User") : SingleWordArgument<Member, MessageCreateEvent>() {
    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Member> {
        val guildId = context.message.guildId ?: return failure("Can't get member outside of guilds.")

        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected discord mention.")
        }

        return when (val member = context.kord.getMember(guildId, snowflake)) {
            null -> failure("User not found.")
            else -> success(member)
        }
    }

    companion object : MemberArgument()

}
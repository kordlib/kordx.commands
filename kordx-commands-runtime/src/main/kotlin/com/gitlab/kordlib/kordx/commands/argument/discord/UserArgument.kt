package com.gitlab.kordlib.kordx.commands.argument.discord

import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.*

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

open class UserSnowflakeArgument(override val name: String = "User") : SingleWordArgument<Snowflake, MessageCreateEvent>() {

    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected Discord Mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : UserSnowflakeArgument()
}


open class UserArgument(override val name: String = "User") : SingleWordArgument<User, MessageCreateEvent>() {
    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<User> {
        val number = word.toLongOrNull()
        val snowflake = when {
           number != null -> Snowflake(number)
           word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
           else -> return failure("Expected Discord Mention.")
       }

        return when (val user = context.kord.getUser(snowflake)) {
            null -> failure("User Not Found.")
            else -> success(user)
        }
    }

    companion object : UserArgument()
}

package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.behavior.UserBehavior
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.result.Result

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

open class UserArgument(override val name: String = "User") : SingleWordArgument<User, MessageCreateEvent>() {
    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: MessageCreateEvent): Result<User> {
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val user = context.kord.getUser(snowflake)) {
            null -> failure("User not found.")
            else -> success(user)
        }
    }

    companion object : UserArgument()

}
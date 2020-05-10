package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

internal class InternalUserArgument(override val name: String = "User") : SingleWordArgument<User, MessageCreateEvent>() {
    override val example: String
        get() = "@User"

    override suspend fun parse(word: String, context: MessageCreateEvent): ArgumentResult<User> {
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

}

val UserArgument: Argument<User, MessageCreateEvent> = InternalUserArgument()

@Suppress("FunctionName")
fun UserArgument(name: String): Argument<User, MessageCreateEvent> = InternalUserArgument(name)

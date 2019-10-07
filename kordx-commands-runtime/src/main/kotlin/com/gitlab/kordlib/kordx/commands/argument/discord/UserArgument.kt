package com.gitlab.kordlib.kordx.commands.argument.discord

import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.kordx.commands.argument.*

private val mentionRegex = Regex("""^<(@|@!)\d+>$""")

open class UserSnowflakeArgument(override val name: String = "User") : SingleWordArgument<Snowflake>() {

    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: ParsingContext): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected discord mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : UserSnowflakeArgument()
}


open class UserArgument(override val name: String = "User") : SingleWordArgument<User>() {
    final override val example: String
        get() = "@User"

    final override suspend fun parse(word: String, context: ParsingContext): Result<User> {
        val number = word.toLongOrNull()
        val snowflake = when {
           number != null -> Snowflake(number)
           word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
           else -> return failure("Expected discord mention.")
       }

        return when (val user = context.kord.getUser(snowflake)) {
            null -> failure("User not found.")
            else -> success(user)
        }
    }

    companion object : UserArgument()
}

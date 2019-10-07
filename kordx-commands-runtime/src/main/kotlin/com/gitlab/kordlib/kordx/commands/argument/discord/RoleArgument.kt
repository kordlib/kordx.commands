package com.gitlab.kordlib.kordx.commands.argument.discord

import com.gitlab.kordlib.core.entity.Role
import com.gitlab.kordlib.core.entity.Snowflake
import com.gitlab.kordlib.kordx.commands.argument.ParsingContext
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<#&\d+>$""")

open class RoleSnowflakeArgument(override val name: String = "Role") : SingleWordArgument<Snowflake>() {

    final override val example: String
        get() = "@Role"

    final override suspend fun parse(word: String, context: ParsingContext): Result<Snowflake> {
        if (!word.matches(mentionRegex)) return failure("Expected discord mention.")

        val snowflake = word.removeSuffix(">").dropWhile { !it.isDigit() }
        return success(Snowflake(snowflake))
    }

    companion object : RoleSnowflakeArgument()

}

open class RoleArgument(override val name: String = "Role") : SingleWordArgument<Role>() {

    final override val example: String
        get() = "@Role"

    final override suspend fun parse(word: String, context: ParsingContext): Result<Role> {
        val guildId = context.event.message.guildId ?: return failure("Can't get role outside of guilds.")
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected discord mention.")
        }

        return when (val role = context.kord.getRole(guildId, snowflake)) {
            null -> failure("Role not found.")
            else -> success(role)
        }
    }

    companion object : RoleArgument()

}

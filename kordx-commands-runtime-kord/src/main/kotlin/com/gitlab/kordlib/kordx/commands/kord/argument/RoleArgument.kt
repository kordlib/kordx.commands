package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.Role
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<#&\d+>$""")

internal class InternalRoleArgument(
        override val name: String = "Role"
) : SingleWordArgument<Role, MessageCreateEvent>() {

    override val example: String
        get() = "@Role"

    override suspend fun parse(word: String, context: MessageCreateEvent): ArgumentResult<Role> {
        val guildId = context.message.getGuildOrNull()?.id ?: return failure("Can't get role outside of guilds.")
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val role = context.kord.getGuild(guildId)?.getRole(snowflake)) {
            null -> failure("Role not found.")
            else -> success(role)
        }
    }

}

/**
 * Argument that matches a role mention or a role id as a number.
 */
val RoleArgument: Argument<Role, MessageCreateEvent> = InternalRoleArgument()

/**
 * Argument that matches a role mention or a role id as a number.
 *
 * @param name The name of this argument.
 */
@Suppress("FunctionName")
fun RoleArgument(name: String): Argument<Role, MessageCreateEvent> = InternalRoleArgument(name)

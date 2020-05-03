package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.Role
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.SingleWordArgument

private val mentionRegex = Regex("""^<#&\d+>$""")

internal class InternalRoleArgument(override val name: String = "Role") : SingleWordArgument<Role, MessageCreateEvent>() {

    override val example: String
        get() = "@Role"

    override suspend fun parse(word: String, context: MessageCreateEvent): Result<Role> {
        val guildId = context.message.guildId ?: return failure("Can't get role outside of guilds.")
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val role = context.kord.getRole(guildId, snowflake)) {
            null -> failure("Role not found.")
            else -> success(role)
        }
    }

}

val RoleArgument: Argument<Role, MessageCreateEvent> = InternalRoleArgument()

@Suppress("FunctionName")
fun RoleArgument(name: String) : Argument<Role, MessageCreateEvent> = InternalRoleArgument(name)
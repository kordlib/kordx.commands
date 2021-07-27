package dev.kord.x.commands.kord.argument

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Role
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.SingleWordArgument
import dev.kord.x.commands.argument.result.WordResult

private val mentionRegex = Regex("""^<@&\d+>$""")

internal class InternalRoleArgument(
    override val name: String = "Role"
) : SingleWordArgument<Role, MessageCreateEvent>() {

    override suspend fun parse(word: String, context: MessageCreateEvent): WordResult<Role> {
        val guildId = context.guildId ?: return failure("Can't get role outside of guilds.")
        val number = word.toLongOrNull()
        val snowflake = when {
            number != null -> Snowflake(number)
            word.matches(mentionRegex) -> Snowflake(word.removeSuffix(">").dropWhile { !it.isDigit() })
            else -> return failure("Expected mention.")
        }

        return when (val role = context.kord.getGuild(guildId)?.getRoleOrNull(snowflake)) {
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

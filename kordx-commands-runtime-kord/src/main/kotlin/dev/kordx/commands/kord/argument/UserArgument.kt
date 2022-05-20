package dev.kordx.commands.kord.argument

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.argument.Argument
import dev.kordx.commands.argument.SingleWordArgument
import dev.kordx.commands.argument.result.WordResult

private val mentionRegex = Regex("""^<@!?\d+>$""")

internal class InternalUserArgument(
        override val name: String = "User"
) : SingleWordArgument<User, MessageCreateEvent>() {

    override suspend fun parse(word: String, context: MessageCreateEvent): WordResult<User> {
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

/**
 * Argument that matches against a user/member mention or a user id as a number.
 */
val UserArgument: Argument<User, MessageCreateEvent> = InternalUserArgument()

/**
 * Argument that matches against a user/member mention or a user id as a number.
 *
 * @param name The name of this argument.
 */
@Suppress("FunctionName")
fun UserArgument(name: String): Argument<User, MessageCreateEvent> = InternalUserArgument(name)

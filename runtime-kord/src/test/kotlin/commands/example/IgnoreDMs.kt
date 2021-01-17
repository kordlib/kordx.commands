@file:AutoWired

package commands.example

import dev.kord.common.entity.Snowflake
import dev.kord.x.commands.annotation.AutoWired
import dev.kord.x.commands.kord.model.processor.eventFilter
import org.koin.dsl.module

/**
 * Register our UserResponses dependency.
 */
val userResponseDependencies = module {
    single { UserResponses() }
}

/**
 * Container that tracks if we've told the user that we're not responding to their DMs
 */
class UserResponses(val users: MutableMap<Snowflake, Boolean> = mutableMapOf())

/**
 * Ignore the messages from DMs, tell the user to flip off once.
 */
fun guildOnly(responses: UserResponses) = eventFilter {
    (message.getGuildOrNull() != null).also {
        if (!it && responses.users[message.author!!.id] != true) {
            message.channel.createMessage("I only respond to guild messages.")
            responses.users[message.author!!.id] == true
        }
    }
}


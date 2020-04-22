@file:AutoWired

package commands.example

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.kord.eventFilter
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
    (message.guildId != null).also {
        if (!it && responses.users[message.author!!.id] != true) {
            message.channel.createMessage("I only respond to guild messages.")
            responses.users[message.author!!.id] == true
        }
    }
}


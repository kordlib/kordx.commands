@file:AutoWired

package commands.example

import dev.kord.core.Kord
import dev.kord.x.commands.annotation.AutoWired
import dev.kord.x.commands.kord.bot
import dev.kord.x.commands.kord.model.prefix.kord
import dev.kord.x.commands.kord.model.prefix.mention
import dev.kord.x.commands.model.prefix.literal
import dev.kord.x.commands.model.prefix.or
import dev.kord.x.commands.model.prefix.prefix
import kapt.kotlin.generated.configure
import kotlinx.coroutines.Dispatchers

/**
 * entry method, `configure` is generated on build.
 */
suspend fun main() = bot(buildKord()) {
    configure()
}

suspend fun buildKord() = Kord(System.getenv("token")) {
    defaultDispatcher = Dispatchers.Default
    cache {
        disableAll()
    }
}

/**
 * Configure the prefix for Kord. We'll have the bot listen to '!' and mentions.
 */
val prefix = prefix {
    kord { literal("!") or mention() }
}

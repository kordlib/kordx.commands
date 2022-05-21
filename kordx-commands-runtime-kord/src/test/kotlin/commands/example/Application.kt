@file:dev.kordx.commands.annotation.AutoWired

package commands.example

import dev.kord.core.Kord
import dev.kordx.commands.kord.bot
import dev.kordx.commands.kord.model.prefix.kord
import dev.kordx.commands.kord.model.prefix.mention
import dev.kordx.commands.model.prefix.literal
import dev.kordx.commands.model.prefix.or
import dev.kordx.commands.model.prefix.prefix
import kotlinx.coroutines.Dispatchers
import kapt.kotlin.generated.configure

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

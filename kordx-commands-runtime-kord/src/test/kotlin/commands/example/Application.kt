@file:AutoWired

package commands.example

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.kord.bot
import com.gitlab.kordlib.kordx.commands.kord.model.prefix.kord
import com.gitlab.kordlib.kordx.commands.kord.model.prefix.mention
import com.gitlab.kordlib.kordx.commands.model.prefix.literal
import com.gitlab.kordlib.kordx.commands.model.prefix.or
import com.gitlab.kordlib.kordx.commands.model.prefix.prefix
import kapt.kotlin.generated.configure
import kotlinx.coroutines.Dispatchers

/**
 * Entry method, `configure` is generated on build.
 */
suspend fun main() {
    Examples.way1()
    // Examples.way2()
}

@Suppress("UNUSED_VARIABLE")
object Examples {
    /**
     * Way 1: Usage with token and default settings of [com.gitlab.kordlib.core.builder.kord.KordBuilder].
     */
    suspend fun way1() {
         // If login is passed as true, there is no need of manually invoking Kord.login()
         // and it will suspend this line until the gateway until logged out.
        val (kord, commandProcessor) = bot("token", login = false) {
            configure()
        }
        // ...
        kord.login()
    }

    /**
     * Way 2: Create with a [Kord] instance.
     */
    suspend fun way2() {
        val kord = buildKord()

        // If login is passed as true, there is no need of manually invoking Kord.login()
        // and it will suspend this line until the gateway until logged out.
        val commandsProcessor = bot(kord, login = false) {
            configure()
        }
        // ...
        kord.login()
    }
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

@file:OptIn(ExperimentalStdlibApi::class)

package commands.example

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.behavior.edit
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.argument.text.StringArgument
import com.gitlab.kordlib.kordx.commands.kord.bot
import com.gitlab.kordlib.kordx.commands.kord.commandCache
import com.gitlab.kordlib.kordx.commands.kord.module.commands
import com.gitlab.kordlib.kordx.commands.model.command.invoke
import kapt.kotlin.generated.configure
import kotlinx.coroutines.Dispatchers

suspend fun main() {
    val kord = Kord(System.getenv("token")) {
        defaultDispatcher = Dispatchers.Default
        cache {
            disableAll()
            commandCache(lruCache(size = 100))  // Configure the cache
        }
    }

    bot(kord) {
        configure()
        registerCommandCache()  // register the cache, so become usable.
    }
}

/**
 * Configure the prefix for Kord. We'll have the bot listen to '!' and mentions.
 */
// Already defined in this package in Application.kt.
// @AutoWired
//val prefix = prefix {
//    kord { literal("!") or mention() }
//}


/**
 * Example of command caching.
 */
@AutoWired
@ModuleName("cache")
fun cacheInMessageEdit() = commands {

    command("cache") {

        invoke(StringArgument) { a, cache ->

            val oldCache = cache.getOrNull<Pair<String, Message>>()

            val sentMessage =
                    if (oldCache != null) {
                        val (txt, msg) = oldCache
                        val toSend = buildString {
                            appendLine("Argument in your message before edit: $txt")
                            appendLine("Argument in your message currently: $a")
                        }

                        msg.edit { content = toSend }
                    } else {
                        respond("Argument in your message currently: $a")
                    }

            cache.setOrUpdate(a to sentMessage)

        }

    }

}

@file:AutoWired

package commands.example

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.kord.bot
import com.gitlab.kordlib.kordx.commands.kord.model.prefix.kord
import com.gitlab.kordlib.kordx.commands.model.prefix.prefix
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
 * Configure the prefix for Kord.
 */
val prefix = prefix {
    kord { "!" }
}


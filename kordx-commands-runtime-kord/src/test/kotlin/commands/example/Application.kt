@file:AutoWired

package commands.example

import com.gitlab.kordlib.cache.api.DataEntryCache
import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.event.guild.MemberJoinEvent
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.argument.primitives.DoubleArgument
import com.gitlab.kordlib.kordx.commands.argument.text.WordArgument
import com.gitlab.kordlib.kordx.commands.command.Command
import com.gitlab.kordlib.kordx.commands.command.command
import com.gitlab.kordlib.kordx.commands.command.invoke
import com.gitlab.kordlib.kordx.commands.kord.*
import com.gitlab.kordlib.kordx.commands.kord.listeners.on
import com.gitlab.kordlib.kordx.commands.pipe.prefix
import com.gitlab.kordlib.kordx.emoji.Emojis
import kapt.kotlin.generated.configure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.koin.core.get
import org.koin.dsl.module
import kotlin.collections.set

/**
 * entry method, `configure` is generated on build.
 */
suspend fun main() = bot(kord()) {
    configure()
}

suspend fun kord() = Kord(System.getenv("token")) {
    defaultDispatcher = Dispatchers.Default
    cache {
        defaultGenerator = { _, _ -> DataEntryCache.none() }
    }
}

/**
 * Configure the prefix for Kord.
 */
val prefix = prefix {
    add<Kord> { "!" }
}


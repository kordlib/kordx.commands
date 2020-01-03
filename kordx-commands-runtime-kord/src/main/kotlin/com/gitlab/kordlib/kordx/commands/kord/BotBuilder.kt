package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.builder.kord.KordBuilder
import com.gitlab.kordlib.core.event.Event
import com.gitlab.kordlib.core.kordLogger
import com.gitlab.kordlib.core.on
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventSource
import com.gitlab.kordlib.kordx.commands.kord.listeners.EventListener
import com.gitlab.kordlib.kordx.commands.pipe.PipeConfig
import com.gitlab.kordlib.kordx.commands.pipe.Prefix
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class BotBuilder(token: String) {
    val kordBuilder: KordBuilder = KordBuilder(token)
    val pipeConfig: PipeConfig = PipeConfig()
    private val eventListeners = mutableListOf<EventListener<Event>>()

    val ignoreSelf = eventFilter { message.author?.id != kord.selfId }
    val ignoreBots = eventFilter { message.author?.isBot != true }

    init {
        pipe {
            +ignoreBots
            +ignoreSelf
        }
    }

    operator fun<T: Event> EventListener<T>.unaryPlus() {
        eventListeners.add(this as EventListener<Event>)
    }

    inline fun kord(builder: KordBuilder.() -> Unit) {
        kordBuilder.apply(builder)
    }

    inline fun pipe(builder: PipeConfig.() -> Unit) {
        pipeConfig.apply(builder)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun build() {
        val kord = kordBuilder.build()

        eventListeners.forEach {listener ->
            kord.events.buffer(Channel.UNLIMITED).filter { listener.matches(it) }.onEach {
                runCatching { listener.consume(it) }.onFailure { kordLogger.catching(it) }
            }.catch { kordLogger.catching(it) }.launchIn(kord)
        }

        pipeConfig.apply {
            eventSources += KordEventSource(kord)
            if (prefixes.isEmpty()) {
                prefixes += Prefix.literal("+")
            }
        }.build()

        kord.login()
    }

}

suspend inline fun bot(token: String, builder: BotBuilder.() -> Unit) {
    BotBuilder(token).apply(builder).build()
}

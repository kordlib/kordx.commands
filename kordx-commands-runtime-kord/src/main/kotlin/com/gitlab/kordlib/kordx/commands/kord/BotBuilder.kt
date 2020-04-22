package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.command.CommonContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordContextConverter
import com.gitlab.kordlib.kordx.commands.kord.context.KordErrorHandler
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventSource
import com.gitlab.kordlib.kordx.commands.kord.listeners.EventListener
import com.gitlab.kordlib.kordx.commands.pipe.BaseEventHandler
import com.gitlab.kordlib.kordx.commands.pipe.PipeConfig
import mu.KotlinLogging
import org.koin.core.get

private val logger = KotlinLogging.logger("[kordx.commands]:[kord]:[BotBuilder]")

fun PipeConfig.addListener(vararg listeners: EventListener<*>) {
    listeners.forEach { listener ->
        with(listener) { get<Kord>().apply() }
    }
}

class BotBuilder(val kord: Kord, val pipeConfig: PipeConfig = PipeConfig()) {

    val ignoreSelf = eventFilter { message.author?.id != kord.selfId }
    val ignoreBots = eventFilter { message.author?.isBot != true }

    init {
        pipe {
            +ignoreBots
            +ignoreSelf
        }
    }

    inline fun pipe(builder: PipeConfig.() -> Unit) {
        pipeConfig.apply(builder)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun build() {

        pipeConfig.apply {
            eventSources += KordEventSource(kord)

            if (eventHandlers[KordContext] == null && eventHandlers[CommonContext] == null) {
                eventHandlers[KordContext] = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())
            }

            if (prefixBuilder.suppliers[KordContext] == null && prefixBuilder.suppliers[CommonContext] == null) {
                logger.warn {
                    """
                    You currently don't have a prefix registered for Kord, allowing users to accidentally invoke a command when they don't intend to.
                    Consider setting a prefix for the KordContext or CommonContext.
                """.trimIndent()
                }
            }
        }.build()

        kord.login()
    }

}

suspend inline fun bot(token: String, configure: PipeConfig.() -> Unit) = bot(Kord(token), configure)

suspend inline fun bot(kord: Kord, configure: PipeConfig.() -> Unit) {
    val builder = BotBuilder(kord)
    builder.pipe {
        koin {
            modules(org.koin.dsl.module { single { kord } })
        }
    }
    builder.pipeConfig.configure()
    builder.build()
}

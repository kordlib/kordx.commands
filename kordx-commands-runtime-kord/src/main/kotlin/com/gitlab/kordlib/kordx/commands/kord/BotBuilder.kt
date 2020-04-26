package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.kord.model.processor.*
import com.gitlab.kordlib.kordx.commands.kord.plug.EventPlug
import com.gitlab.kordlib.kordx.commands.kord.plug.KordPlugHandler
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.BaseEventHandler
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorConfig
import mu.KotlinLogging
import org.koin.dsl.module

private val logger = KotlinLogging.logger {}

class BotBuilder(val kord: Kord, val processorConfig: KordProcessorConfig = KordProcessorConfig(kord)) {

    val ignoreSelf = eventFilter { message.author?.id != kord.selfId }
    val ignoreBots = eventFilter { message.author?.isBot != true }

    init {
        processor {
            +ignoreBots
            +ignoreSelf
        }
        processorConfig.plugSockets[EventPlug.Key] = KordPlugHandler(kord)
        processor {
            koin {
                modules(module { single { kord } })
            }
        }
    }

    inline fun processor(builder: ProcessorConfig.() -> Unit) {
        processorConfig.apply(builder)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun build() {

        processorConfig.apply {
            eventSources += KordEventSource(kord)

            if (eventHandlers[KordContext] == null && eventHandlers[CommonContext] == null) {
                eventHandlers[KordContext] = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())
            }

            if (prefixBuilder.suppliers[KordContext] == null && prefixBuilder.suppliers[CommonContext] == null) {
                val message = """
                    You currently don't have a prefix registered for Kord, allowing users to accidentally invoke a command when they don't intend to.
                    Consider setting a prefix for the KordContext or CommonContext.
                """.trimIndent()

                if (logger.isWarnEnabled) {
                    logger.warn { message }
                } else {
                    System.err.println(message)
                }

            }
        }.build()

        kord.login()
    }

}

suspend inline fun bot(token: String, configure: KordProcessorConfig.() -> Unit) = bot(Kord(token), configure)

suspend inline fun bot(kord: Kord, configure: KordProcessorConfig.() -> Unit) {
    val builder = BotBuilder(kord)
    builder.processorConfig.configure()
    builder.build()
}

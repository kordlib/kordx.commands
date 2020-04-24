package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.kord.listeners.EventListener
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContextConverter
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordErrorHandler
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventSource
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.BaseEventHandler
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorConfig
import mu.KotlinLogging
import org.koin.core.get

private val logger = KotlinLogging.logger {}

fun ProcessorConfig.addListener(vararg listeners: EventListener<*>) {
    listeners.forEach { listener ->
        with(listener) { get<Kord>().apply() }
    }
}

class BotBuilder(val kord: Kord, val processorConfig: ProcessorConfig = ProcessorConfig()) {

    val ignoreSelf = eventFilter { message.author?.id != kord.selfId }
    val ignoreBots = eventFilter { message.author?.isBot != true }

    init {
        processor {
            +ignoreBots
            +ignoreSelf
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

suspend inline fun bot(token: String, configure: ProcessorConfig.() -> Unit) = bot(Kord(token), configure)

suspend inline fun bot(kord: Kord, configure: ProcessorConfig.() -> Unit) {
    val builder = BotBuilder(kord)
    builder.processor {
        koin {
            modules(org.koin.dsl.module { single { kord } })
        }
    }
    builder.processorConfig.configure()
    builder.build()
}

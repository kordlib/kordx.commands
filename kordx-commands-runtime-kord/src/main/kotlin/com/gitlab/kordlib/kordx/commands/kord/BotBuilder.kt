package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContextConverter
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordErrorHandler
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventSource
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordProcessorBuilder
import com.gitlab.kordlib.kordx.commands.kord.model.processor.eventFilter
import com.gitlab.kordlib.kordx.commands.kord.plug.KordPlugSocket
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.BaseEventHandler
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import com.gitlab.kordlib.kordx.commands.model.processor.EventHandler
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorBuilder
import mu.KotlinLogging
import org.koin.dsl.module

private val logger = KotlinLogging.logger {}

private const val NO_PREFIX_MESSAGE = """
You currently don't have a prefix registered for Kord, allowing users to accidentally invoke commands.
Consider setting a prefix for the KordContext or CommonContext.
"""

/**
 *
 * Builder for a Discord bot using [kord].
 *
 * @param processorBuilder the builder this class wraps around.
 */
class BotBuilder(
        val kord: Kord,
        val processorBuilder: KordProcessorBuilder = KordProcessorBuilder(kord)
) {

    /**
     * Filter that will ignore all events created by the bot itself, added by default
     * Can be disabled by invoking the `unaryMinus` operator inside the configuration.
     * ```kotlin
     * {
     *     -ignoreSelf
     * }
     * ```
     */
    val ignoreSelf = eventFilter { message.author?.id != kord.selfId }

    /**
     * Filter that will ignore all events created by bots, added by default.
     * Can be disabled by invoking the `unaryMinus` operator inside the configuration.
     * ```kotlin
     * {
     *     -ignoreBots
     * }
     * ```
     */
    val ignoreBots = eventFilter { message.author?.isBot != true }

    init {
        processor {
            +ignoreBots
            +ignoreSelf
        }


        processor {
            koin {
                modules(module { single { kord } })
            }
            +KordPlugSocket(kord)
        }
    }

    /**
     * Configures the [processorBuilder].
     */
    inline fun processor(builder: ProcessorBuilder.() -> Unit) {
        processorBuilder.apply(builder)
    }

    /**
     * Builds the [processorBuilder], raising a warning if no prefix has been set.
     *
     * If no [EventHandler] was supplied for the [CommonContext]
     */
    @Suppress("EXPERIMENTAL_API_USAGE")
    suspend fun build(): CommandProcessor = processorBuilder.apply {
        eventSources += KordEventSource(kord)

        if (eventHandlers[KordContext] == null && eventHandlers[CommonContext] == null) {
            eventHandlers[KordContext] = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())
        }

        if (prefixBuilder[KordContext] == null && prefixBuilder[CommonContext] == null) {
            if (logger.isWarnEnabled) {
                logger.warn { NO_PREFIX_MESSAGE }
            } else {
                System.err.println(NO_PREFIX_MESSAGE)
            }
        }
    }.build()

}

/**
 * Creates a bot with the given [token] as discord bot token, applying [configure] to the bot's configuration.
 * Once created, the bot will log in to the gateway and suspend until the gateway until logged out.
 */
@Deprecated(
    message = "Use bot(token, login, configure) instead.",
    replaceWith = ReplaceWith("bot(token, login = true, configure)"),
    level = DeprecationLevel.WARNING
)
suspend inline fun bot(token: String, configure: KordProcessorBuilder.() -> Unit) {
    bot(Kord(token), login = true, configure)
}

/**
 * Creates a bot with the given [kord] instance, applying [configure] to the bot's configuration.
 * Once created, the bot will log in to the gateway and suspend until the gateway until logged out.
 */
@Deprecated(
    message = "Use bot(kord, login, configure) instead.",
    replaceWith = ReplaceWith("bot(kord, login = true, configure)"),
    level = DeprecationLevel.WARNING
)
suspend inline fun bot(kord: Kord, configure: KordProcessorBuilder.() -> Unit) {
    bot(kord, login = true, configure)
}

/**
 * Creates a bot with the given [token] as discord bot token, applying [configure] to the bot's configuration.
 * If [login] is true, the bot will log in to the gateway and suspend until the gateway until logged out.
 */
suspend inline fun bot(
        token: String,
        login: Boolean = false,
        configure: KordProcessorBuilder.() -> Unit
): Pair<Kord, CommandProcessor> {
    val kord = Kord(token)
    val commandProcessor = bot(kord, login, configure)
    return Pair(kord, commandProcessor)
}

/**
 * Creates a bot with the given [kord] instance, applying [configure] to the bot's configuration.
 * If [login] is true, the bot will log in to the gateway and suspend until the gateway logged out.
 */
suspend inline fun bot(
        kord: Kord,
        login: Boolean,
        configure: KordProcessorBuilder.() -> Unit
): CommandProcessor =
        BotBuilder(kord)
                .apply { processorBuilder.configure() }
                .build()
                .also { if (login) kord.login() }

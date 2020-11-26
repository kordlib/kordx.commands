package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.kord.cache.KordCommandCache
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorBuilder

private const val PREFIX_FLAG_DEPRECATED_MESSAGE = """
'enableMentionPrefix' is deprecated and has been implemented as a prefix rule instead, 
replace it with `or mention()` in the kord prefix configuration.
"""

/**
 * ProcessorBuilder with extra utility for the [KordContext].
 *
 * @param kord the Kord instance from which events will be read.
 */
class KordProcessorBuilder(val kord: Kord) : ProcessorBuilder() {

    /**
     * True if `@bot` mentions should work as a substitute for the prefix. Default is `true`.
     */
    @Deprecated(PREFIX_FLAG_DEPRECATED_MESSAGE, level = DeprecationLevel.ERROR)
    var enableMentionPrefix: Boolean
        get() = error(PREFIX_FLAG_DEPRECATED_MESSAGE)
        set(_) = error(PREFIX_FLAG_DEPRECATED_MESSAGE)

    /**
     * Registers command cache, if want to cache the first/prior run results to handle message update events.
     * Configure its behavior at the time of creation of [Kord] instance by
     * [com.gitlab.kordlib.kordx.commands.kord.commandCache].
     *
     * As an example to cache the sent message id, and if command updates use that id to edit the sent message.
     */
    suspend fun registerCommandCache() {
        kord.cache.register(KordCommandCache.description)
    }

    override suspend fun build(): CommandProcessor {
        return super.build()
    }

}

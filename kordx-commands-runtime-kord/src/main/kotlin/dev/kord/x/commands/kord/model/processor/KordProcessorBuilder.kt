package dev.kord.x.commands.kord.model.processor

import dev.kord.core.Kord
import dev.kord.x.commands.model.processor.CommandProcessor
import dev.kord.x.commands.model.processor.ProcessorBuilder

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
        set(value) = error(PREFIX_FLAG_DEPRECATED_MESSAGE)

    override suspend fun build(): CommandProcessor {
        return super.build()
    }

}

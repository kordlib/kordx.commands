package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.prefix.mentionPrefix
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixSupplier
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorBuilder

/**
 * ProcessorBuilder with extra utility for the [KordContext].
 *
 * @param kord the Kord instance from which events will be read.
 */
class KordProcessorBuilder(val kord: Kord) : ProcessorBuilder() {

    /**
     * True if `@bot` mentions should work as a substitute for the prefix. Default is `true`.
     */
    var enableMentionPrefix: Boolean = true

    override suspend fun build(): CommandProcessor {
        if (enableMentionPrefix) {
            val prefix: PrefixSupplier<MessageCreateEvent>? = (
                    prefixBuilder[KordContext] ?: prefixBuilder[CommonContext]
                    )
            val newPrefix: PrefixSupplier<MessageCreateEvent> = mentionPrefix(kord.selfId, prefix)
            prefixBuilder.add(KordContext, newPrefix)
        }

        return super.build()
    }

}

package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.prefix.mentionPrefix
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixSupplier
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorConfig

class KordProcessorConfig(val kord: Kord) : ProcessorConfig() {

    var enableMentionPrefix: Boolean = true

    override suspend fun build(): CommandProcessor {
        if (enableMentionPrefix) {
            val prefix: PrefixSupplier<MessageCreateEvent>? = (
                    prefixBuilder.suppliers[KordContext] ?: prefixBuilder.suppliers[CommonContext]
                    ) as? PrefixSupplier<MessageCreateEvent>
            val newPrefix: PrefixSupplier<MessageCreateEvent> = mentionPrefix(kord.selfId, prefix)
            prefixBuilder.add(KordContext, newPrefix)
        }

        return super.build()
    }

}
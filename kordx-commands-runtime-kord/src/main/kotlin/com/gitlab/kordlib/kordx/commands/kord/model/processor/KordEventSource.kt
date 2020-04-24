package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import com.gitlab.kordlib.kordx.commands.model.processor.EventSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class KordEventSource(
        val kord: Kord
) : EventSource<MessageCreateEvent> {
    override val context: ProcessorContext<MessageCreateEvent, *, *>
        get() = KordContext

    override val events: Flow<MessageCreateEvent>
        get() = kord.events.filterIsInstance()

}

package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import com.gitlab.kordlib.kordx.commands.model.processor.EventSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterIsInstance

/**
 * [EventSource] for [Kord.events], these can be consumed under a [KordContext].
 *
 * @param kord the Kord instance from which events are read.
 */
class KordEventSource(
        val kord: Kord
) : EventSource<MessageCreateEvent> {
    override val context: ProcessorContext<MessageCreateEvent, *, *>
        get() = KordContext

    override val events: Flow<MessageCreateEvent>
        get() = kord.events.buffer(Channel.UNLIMITED).filterIsInstance()

}

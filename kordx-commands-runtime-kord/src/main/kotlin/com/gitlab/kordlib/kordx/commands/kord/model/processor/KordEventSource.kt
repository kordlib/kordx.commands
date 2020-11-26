package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.model.processor.EventSource
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer

/**
 * [EventSource] for [Kord.events], these can be consumed under a [KordContext].
 *
 * @param kord the Kord instance from which events are read.
 */
class KordEventSource(
        val kord: Kord
) : EventSource<KordEventAdapter> {
    override val context: ProcessorContext<KordEventAdapter, *, *>
        get() = KordContext

    override val events: Flow<KordEventAdapter>
        get() = kord.events.buffer(Channel.UNLIMITED).filterAndMapToAdapter()
}

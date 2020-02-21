package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.ContextConverter
import com.gitlab.kordlib.kordx.commands.kord.CommandSuggester
import com.gitlab.kordlib.kordx.commands.pipe.EventSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class KordEventSource(
        val kord: Kord,
        override val converter: ContextConverter<MessageCreateEvent, MessageCreateEvent, KordEventContext> = KordConverter(CommandSuggester)
) : EventSource<MessageCreateEvent> {
    override val context: CommandContext<MessageCreateEvent, *, *>
        get() = KordContext

    override val events: Flow<MessageCreateEvent>
        get() = kord.events.filterIsInstance()

}

package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.KordObject
import com.gitlab.kordlib.core.behavior.channel.createEmbed
import com.gitlab.kordlib.core.behavior.channel.createMessage
import com.gitlab.kordlib.core.builder.message.EmbedBuilder
import com.gitlab.kordlib.core.builder.message.MessageCreateBuilder
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.command.Command
import com.gitlab.kordlib.kordx.commands.command.EventContext
import kotlinx.coroutines.flow.*

data class KordEventContext(
        override val event: MessageCreateEvent,
        override val command: Command<*>
) : EventContext, KordEvent, KordObject {
    override val module get() = command.module
    override val kord: Kord get() = super.kord

    override suspend fun respond(text: String): Message {
        return message.channel.createMessage(text)
    }

}

package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.KordObject
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.Command

data class KordEventContext(
        override val event: MessageCreateEvent,
        val command: Command<*>
) : KordEvent, KordObject {
    val module get() = command.module
    override val kord: Kord get() = super.kord

    override suspend fun respond(text: String): Message {
        return message.channel.createMessage(text)
    }

}

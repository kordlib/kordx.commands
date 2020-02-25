package com.gitlab.kordlib.kordx.commands.kord.context

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.KordObject
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.Command
import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.Module
import org.koin.core.Koin

data class KordEventContext(
        override val event: MessageCreateEvent,
        override val command: Command<*>,
        override val commands: Map<String, Command<*>>,
        private val koin: Koin
) : CommandContext, KordEvent, KordObject {
    override fun getKoin(): Koin = koin

    override val module get() = command.module
    override val kord: Kord get() = super.kord
}

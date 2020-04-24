package com.gitlab.kordlib.kordx.commands.kord.model.context

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.KordObject
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.KordEvent
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import org.koin.core.Koin

data class KordCommandContext(
        override val event: MessageCreateEvent,
        override val command: Command<KordCommandContext>,
        override val commands: Map<String, Command<*>>,
        private val koin: Koin,
        override val processor: CommandProcessor
) : CommandContext, KordEvent, KordObject {
    override fun getKoin(): Koin = koin

    override val module get() = command.module
    override val kord: Kord get() = super.kord
}

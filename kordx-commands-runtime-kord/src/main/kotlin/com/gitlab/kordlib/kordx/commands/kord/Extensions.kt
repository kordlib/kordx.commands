package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.command.commands
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.kord.context.KordContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordEvent

inline fun module(name: String, builder: ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEvent>.() -> Unit) =
        com.gitlab.kordlib.kordx.commands.command.module(name, KordContext, builder)

fun precondition(
        priority: Long = 0,
        precondition: suspend KordEvent.(PreconditionResult.Companion) -> PreconditionResult
) = com.gitlab.kordlib.kordx.commands.flow.precondition(KordContext, priority, precondition)

fun commands(
        builder: ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEvent>.() -> Unit
) = commands(KordContext, builder)
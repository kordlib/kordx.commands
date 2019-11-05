package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.command.commands
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.kord.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventContext

inline fun module(name: String, builder: ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>.() -> Unit) =
        com.gitlab.kordlib.kordx.commands.command.module(name, KordCommandContext, builder)

fun precondition(
        priority: Long = 0,
        precondition: suspend KordEventContext.(PreconditionResult.Companion) -> PreconditionResult
) = com.gitlab.kordlib.kordx.commands.flow.precondition(KordCommandContext, priority, precondition)

fun commands(
        builder: ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>.() -> Unit
) = commands(KordCommandContext, builder)
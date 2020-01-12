package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.flow.eventFilter
import com.gitlab.kordlib.kordx.commands.flow.precondition
import com.gitlab.kordlib.kordx.commands.kord.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventContext

inline fun module(name: String, crossinline builder: suspend ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>.() -> Unit) =
        module(name, KordCommandContext) {
            builder()
        }

fun precondition(
        priority: Long = 0,
        precondition: suspend KordEventContext.() -> Boolean
) = precondition(KordCommandContext, priority, precondition)

fun eventFilter(
        builder: suspend MessageCreateEvent.() -> Boolean
): EventFilter<MessageCreateEvent> = eventFilter(KordCommandContext, builder)

fun commands(
        builder: ModuleBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>.() -> Unit
) = commands(KordCommandContext, builder)

fun command(
        name: String,
        builder: CommandBuilder<MessageCreateEvent, MessageCreateEvent, KordEventContext>.() -> Unit
): CommandSet = command(KordCommandContext, name, builder)
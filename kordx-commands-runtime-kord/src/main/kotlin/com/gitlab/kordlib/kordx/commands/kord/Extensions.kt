package com.gitlab.kordlib.kordx.commands.kord

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.flow.eventFilter
import com.gitlab.kordlib.kordx.commands.flow.precondition
import com.gitlab.kordlib.kordx.commands.kord.command.KordCommandBuilder
import com.gitlab.kordlib.kordx.commands.kord.context.KordContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventContext
import com.gitlab.kordlib.kordx.commands.kord.module.KordModuleBuilder
import com.gitlab.kordlib.kordx.commands.pipe.PrefixBuilder
import com.gitlab.kordlib.kordx.commands.pipe.PrefixSupplier

inline fun module(name: String, crossinline builder: suspend KordModuleBuilder.() -> Unit) =
        module(name, KordContext) {
            builder()
        }

@JvmName("kordModule")
@Suppress("FINAL_UPPER_BOUND")
inline fun <reified T: Kord> module(name: String, crossinline builder: suspend KordModuleBuilder.() -> Unit) =
        module(name, KordContext) {
            builder()
        }

fun precondition(
        priority: Long = 0,
        precondition: suspend KordEventContext.() -> Boolean
) = precondition(KordContext, priority, precondition)

fun eventFilter(
        builder: suspend MessageCreateEvent.() -> Boolean
): EventFilter<MessageCreateEvent> = eventFilter(KordContext, builder)

fun commands(
        builder: KordModuleBuilder.() -> Unit
) = commands(KordContext, builder)

@JvmName("KordCommands")
inline fun <reified T : Kord> commands(
        noinline builder: KordModuleBuilder.() -> Unit
) = commands(KordContext, builder)

fun command(
        name: String,
        builder: KordCommandBuilder.() -> Unit
): CommandSet = command(KordContext, name, builder)

@JvmName("kordCommand")
@Suppress("FINAL_UPPER_BOUND")
inline fun <reified T : Kord> command(
        name: String,
        noinline builder: KordCommandBuilder.() -> Unit
): CommandSet = command(KordContext, name, builder)

@JvmName("kordWithContext")
@Suppress("FINAL_UPPER_BOUND")
inline fun <reified T : Kord> ModuleBuilder<*, *, *>.withContext(builder: KordModuleBuilder.() -> Unit) = withContext(KordContext) {
    builder()
}

@JvmName("KordAdd")
@Suppress("FINAL_UPPER_BOUND")
inline fun <reified T : Kord> PrefixBuilder.add(noinline supplier: PrefixSupplier<MessageCreateEvent>) {
    add(KordContext, supplier)
}

package dev.kord.x.commands.kord.model.prefix

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.kord.model.processor.KordContext
import dev.kord.x.commands.model.prefix.PrefixBuilder
import dev.kord.x.commands.model.prefix.PrefixRule

/**
 * Sets the [supplier] as the prefix for the [KordContext].
 */
inline fun PrefixBuilder.kord(supplier: () -> PrefixRule<MessageCreateEvent>) =
    add(KordContext, supplier())

/**
 * Sets the [supplier] as the prefix for the [KordContext].
 */
fun PrefixBuilder.kord(supplier: PrefixRule<MessageCreateEvent>) = add(KordContext, supplier)

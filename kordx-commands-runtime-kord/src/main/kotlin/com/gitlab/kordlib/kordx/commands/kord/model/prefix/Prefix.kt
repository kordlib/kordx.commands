package com.gitlab.kordlib.kordx.commands.kord.model.prefix

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixRule

/**
 * Sets the [supplier] as the prefix for the [KordContext].
 */
inline fun PrefixBuilder.kord(supplier: () -> PrefixRule<MessageCreateEvent>) = add(KordContext, supplier())

/**
 * Sets the [supplier] as the prefix for the [KordContext].
 */
fun PrefixBuilder.kord(supplier: PrefixRule<MessageCreateEvent>) = add(KordContext, supplier)

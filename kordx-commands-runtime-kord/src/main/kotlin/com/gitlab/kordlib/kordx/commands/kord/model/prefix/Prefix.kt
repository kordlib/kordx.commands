package com.gitlab.kordlib.kordx.commands.kord.model.prefix

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder

fun PrefixBuilder.kord(supplier: suspend (MessageCreateEvent) -> String) = add(KordContext, supplier)
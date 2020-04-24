package com.gitlab.kordlib.kordx.commands.kord.model.precondition

import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext

fun precondition(
        priority: Long = 0,
        precondition: suspend KordCommandContext.() -> Boolean
) = com.gitlab.kordlib.kordx.commands.model.precondition.precondition(KordContext, priority, precondition)

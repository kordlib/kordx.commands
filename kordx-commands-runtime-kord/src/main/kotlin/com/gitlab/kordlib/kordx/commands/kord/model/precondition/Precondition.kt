package com.gitlab.kordlib.kordx.commands.kord.model.precondition

import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandContext
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition

/**
 * Defines a [Precondition] for Kord commands. Any [KordCommandContext] that doesn't match the [filter] will not be invoked.
 *
 * Note that preconditions run *before* arguments get parsed, a [KordCommandContext] that passed a precondition is therefore
 * not necessarily invoked.
 *
 * @param priority The priority of this precondition compared to other preconditions. Preconditions with a higher priority
 * will be run before others. This can be used to delay potentially expensive preconditions or define a fixed behavior
 * in side effects.
 */
fun precondition(
        priority: Long = 0,
        filter: suspend KordCommandContext.() -> Boolean
) = com.gitlab.kordlib.kordx.commands.model.precondition.precondition(KordContext, priority, filter)

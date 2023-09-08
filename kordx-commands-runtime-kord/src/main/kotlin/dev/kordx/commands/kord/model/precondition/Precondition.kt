package dev.kordx.commands.kord.model.precondition

import dev.kordx.commands.kord.model.context.KordCommandEvent
import dev.kordx.commands.kord.model.processor.KordContext
import dev.kordx.commands.model.precondition.Precondition
import dev.kordx.commands.model.precondition.precondition

/**
 * Defines a [Precondition] for Kord commands.
 * Any [KordCommandEvent] that doesn't match the [filter] will not be invoked.
 *
 * Note that preconditions run *before* arguments get parsed,
 * a [KordCommandEvent] that passed a precondition is therefore not necessarily invoked.
 *
 * @param priority The priority of this precondition compared to other preconditions.
 * Preconditions with a higher priority will be run before others.
 * This can be used to delay potentially expensive preconditions or define a fixed behavior in side effects.
 */
fun precondition(
        priority: Long = 0,
        filter: suspend KordCommandEvent.() -> Boolean
) = precondition(KordContext, priority, filter)

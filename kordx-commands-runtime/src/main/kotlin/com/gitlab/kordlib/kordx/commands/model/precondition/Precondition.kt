package com.gitlab.kordlib.kordx.commands.model.precondition

import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

interface Precondition<C: CommandContext> {
    val priority: Long get() = 0
    val context: ProcessorContext<*, *, C>
    suspend operator fun invoke(event: C): Boolean
}

/**
 * Defines a [Precondition] for [context] commands. Any command that doesn't match the [filter] will not be invoked.
 *
 * Note that preconditions run *before* arguments get parsed, a command that passed a precondition is therefore
 * not necessarily invoked.
 *
 * @param priority The priority of this precondition compared to other preconditions. Preconditions with a higher priority
 * will be run before others. This can be used to delay potentially expensive preconditions or define a fixed behavior
 * in side effects.
 */
fun <C: CommandContext> precondition(
        context: ProcessorContext<*, *, C>,
        priority: Long = 0,
        precondition: suspend C.() -> Boolean
) = object : Precondition<C> {
    override val priority: Long get() = priority
    override val context: ProcessorContext<*, *, C> = context
    override suspend fun invoke(event: C): Boolean = precondition(event)
}

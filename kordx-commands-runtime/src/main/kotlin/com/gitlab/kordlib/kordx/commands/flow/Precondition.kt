package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.PipeContext

interface Precondition<C: CommandContext> {
    val priority: Long get() = 0
    val context: PipeContext<*, *, C>
    suspend operator fun invoke(event: C): Boolean
}

fun <C: CommandContext> precondition(
        context: PipeContext<*, *, C>,
        priority: Long = 0,
        precondition: suspend C.() -> Boolean
) = object : Precondition<C> {
    override val priority: Long get() = priority
    override val context: PipeContext<*, *, C> = context
    override suspend fun invoke(event: C): Boolean = precondition(event)
}

package com.gitlab.kordlib.kordx.commands.model.precondition

import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

interface Precondition<C: CommandContext> {
    val priority: Long get() = 0
    val context: ProcessorContext<*, *, C>
    suspend operator fun invoke(event: C): Boolean
}

fun <C: CommandContext> precondition(
        context: ProcessorContext<*, *, C>,
        priority: Long = 0,
        precondition: suspend C.() -> Boolean
) = object : Precondition<C> {
    override val priority: Long get() = priority
    override val context: ProcessorContext<*, *, C> = context
    override suspend fun invoke(event: C): Boolean = precondition(event)
}

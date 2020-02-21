package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.CommandContext

interface Precondition<EVENTCONTEXT> {
    val priority: Long get() = 0
    val context: CommandContext<*, *, EVENTCONTEXT>
    suspend operator fun invoke(event: EVENTCONTEXT): Boolean
}

fun <EVENTCONTEXT> precondition(
        context: CommandContext<*, *, EVENTCONTEXT>,
        priority: Long = 0,
        precondition: suspend EVENTCONTEXT.() -> Boolean
) = object : Precondition<EVENTCONTEXT> {
    override val priority: Long get() = priority
    override val context: CommandContext<*, *, EVENTCONTEXT> = context
    override suspend fun invoke(event: EVENTCONTEXT): Boolean = precondition(event)
}

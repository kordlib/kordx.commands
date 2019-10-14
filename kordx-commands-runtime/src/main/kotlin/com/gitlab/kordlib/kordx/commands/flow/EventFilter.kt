package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.CommandContext

interface EventFilter<SOURCECONTEXT> {
    val context: CommandContext<SOURCECONTEXT, *, *>
    suspend operator fun invoke(event: SOURCECONTEXT): Boolean
}

fun <SOURCECONTEXT> eventFilter(context: CommandContext<SOURCECONTEXT, *, *>, filter: SOURCECONTEXT.() -> Boolean) = object : EventFilter<SOURCECONTEXT> {
    override val context: CommandContext<SOURCECONTEXT, *, *> = context

    override suspend fun invoke(event: SOURCECONTEXT): Boolean = filter(event)
}

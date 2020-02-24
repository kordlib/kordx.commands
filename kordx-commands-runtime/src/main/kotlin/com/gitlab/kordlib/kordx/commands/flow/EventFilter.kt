package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.PipeContext

interface EventFilter<S> {
    val context: PipeContext<S, *, *>
    suspend operator fun invoke(event: S): Boolean
}

fun <S> eventFilter(context: PipeContext<S, *, *>, filter: suspend S.() -> Boolean) = object : EventFilter<S> {
    override val context: PipeContext<S, *, *> = context

    override suspend fun invoke(event: S): Boolean = filter(event)
}

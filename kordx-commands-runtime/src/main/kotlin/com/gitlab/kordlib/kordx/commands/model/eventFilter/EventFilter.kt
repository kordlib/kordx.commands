package com.gitlab.kordlib.kordx.commands.model.eventFilter

import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

interface EventFilter<S> {
    val context: ProcessorContext<S, *, *>
    suspend operator fun invoke(event: S): Boolean
}

fun <S> eventFilter(context: ProcessorContext<S, *, *>, filter: suspend S.() -> Boolean) = object : EventFilter<S> {
    override val context: ProcessorContext<S, *, *> = context

    override suspend fun invoke(event: S): Boolean = filter(event)
}

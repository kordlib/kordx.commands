package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.PipeContext
import kotlinx.coroutines.flow.Flow

interface EventSource<S> {
    val events: Flow<S>
    val context: PipeContext<S, *, *>
}
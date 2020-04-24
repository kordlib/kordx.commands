package com.gitlab.kordlib.kordx.commands.model.processor

import kotlinx.coroutines.flow.Flow

interface EventSource<S> {
    val events: Flow<S>
    val context: ProcessorContext<S, *, *>
}
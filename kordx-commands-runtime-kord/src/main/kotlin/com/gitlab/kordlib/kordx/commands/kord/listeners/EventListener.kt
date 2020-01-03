package com.gitlab.kordlib.kordx.commands.kord.listeners

import com.gitlab.kordlib.core.event.Event

interface EventListener<T : Event> {

    fun matches(event: Event): Boolean

    suspend fun consume(event: T)

}

inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit) = object : EventListener<T> {

    override fun matches(event: Event): Boolean = event is T

    override suspend fun consume(event: T) = consumer(event)

}

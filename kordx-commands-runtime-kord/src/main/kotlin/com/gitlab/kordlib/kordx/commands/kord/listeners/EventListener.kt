package com.gitlab.kordlib.kordx.commands.kord.listeners

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.Event
import com.gitlab.kordlib.core.on

interface EventListener<T : Event> {

    fun Kord.apply()

}

inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit) = object : EventListener<T> {

    override fun Kord.apply() {
        this.on<T> { consumer() }
    }

}

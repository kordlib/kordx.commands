package com.gitlab.kordlib.kordx.commands.kord.plug

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.Event
import com.gitlab.kordlib.core.on
import com.gitlab.kordlib.kordx.commands.model.plug.Plug


interface EventPlug<T : Event> : Plug<Kord> {

    override val key: Plug.Key<Kord>
        get() = Key

    object Key : Plug.Key<Kord>

    fun apply(kord: Kord)

}

/**
 * Adds an event listener to the bot, listening to events of type [T] and invoking the [consumer] on each one.
 *
 * > All event listeners run isolated from the command framework. They can not interact with any commands or vice versa,
 * no features are provided past the initial setup.
 */
inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit) = object : EventPlug<T> {

    override fun apply(kord: Kord) {
        kord.on<T> { consumer() }
    }

}

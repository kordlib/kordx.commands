package dev.kordx.commands.kord.plug

import dev.kord.core.Kord
import dev.kord.core.event.Event
import dev.kord.core.on
import dev.kordx.commands.model.plug.Plug

/**
 * A [Plug] that listens to [Kord] events.
 */
interface EventPlug : Plug {

    /**
     * Listens to events from [kord].
     */
    fun apply(kord: Kord)
}

/**
 * Adds an event listener to the bot, listening to events of type [T] and invoking the [consumer] on each one.
 *
 * > All event listeners run isolated from the command framework. They can not interact with any commands or vice versa,
 * no features are provided past the initial setup.
 */
inline fun <reified T : Event> on(noinline consumer: suspend T.() -> Unit) = object : EventPlug {

    override fun apply(kord: Kord) {
        kord.on<T> { consumer() }
    }

}

package dev.kordx.commands.kord.plug

import dev.kord.core.Kord
import dev.kordx.commands.model.plug.PlugContainer
import dev.kordx.commands.model.plug.PlugSocket

/**
 * [PlugSocket] that processes [EventPlugs][EventPlug].
 *
 * @param kord the Kord instance to be passed to the [EventPlugs][EventPlug].
 */
class KordPlugSocket(private val kord: Kord) : PlugSocket {

    override suspend fun handle(container: PlugContainer) {
        container.getPlugs<EventPlug>().forEach { it.apply(kord) }
    }

}

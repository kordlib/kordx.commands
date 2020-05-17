package com.gitlab.kordlib.kordx.commands.kord.plug

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.model.plug.Plug
import com.gitlab.kordlib.kordx.commands.model.plug.PlugContainer
import com.gitlab.kordlib.kordx.commands.model.plug.PlugSocket

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

package com.gitlab.kordlib.kordx.commands.kord.plug

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.model.plug.Plug
import com.gitlab.kordlib.kordx.commands.model.plug.PlugSocket

class KordPlugHandler(private val kord: Kord) : PlugSocket<Kord, EventPlug<*>> {

    override val key: Plug.Key<Kord>
        get() = EventPlug.Key

    override suspend fun handle(plug: List<EventPlug<*>>) {
        plug.forEach { it.apply(kord) }
    }

}
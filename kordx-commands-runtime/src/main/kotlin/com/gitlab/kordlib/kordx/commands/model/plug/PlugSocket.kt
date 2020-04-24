package com.gitlab.kordlib.kordx.commands.model.plug

interface PlugSocket<R, T: Plug<R>> {
    val key: Plug.Key<R>

    suspend fun handle(plug: List<T>)

}
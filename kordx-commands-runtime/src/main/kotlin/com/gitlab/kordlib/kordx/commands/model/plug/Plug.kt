package com.gitlab.kordlib.kordx.commands.model.plug


interface Plug<T> {
    val key: Key<T>

    interface Key<T>
}

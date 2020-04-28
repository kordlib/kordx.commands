package com.gitlab.kordlib.kordx.commands.model.metadata

interface Metadata {
    interface Key<T : Any>

    operator fun <T : Any> get(key: Key<T>): T?

    companion object {
        internal fun from(map: Map<Key<*>, Any>) = object : Metadata {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> get(key: Key<T>): T? {
                return map[key] as? T?
            }
        }
    }
}

internal object EmptyMetaData : Metadata {
    override fun <T : Any> get(key: Metadata.Key<T>): T? = null
}

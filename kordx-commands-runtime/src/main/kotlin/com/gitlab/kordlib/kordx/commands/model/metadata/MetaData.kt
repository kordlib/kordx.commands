package com.gitlab.kordlib.kordx.commands.model.metadata

interface Metadata {
    interface Key<T : Any>

    operator fun <T : Any> get(key: Key<T>): T?

}

interface MutableMetadata : Metadata {
    operator fun <T : Any> set(key: Metadata.Key<T>, value: T)

    fun remove(key: Metadata.Key<*>)

    companion object {
        operator fun invoke() = object : MutableMetadata {
            private val map = mutableMapOf<Metadata.Key<*>, Any>()

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> get(key: Metadata.Key<T>): T? {
                return map[key] as? T?
            }

            override fun <T : Any> set(key: Metadata.Key<T>, value: T) {
                map[key] = value
            }

            override fun remove(key: Metadata.Key<*>) {
                map.remove(key)
            }
        }
    }
}
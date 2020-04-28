package com.gitlab.kordlib.kordx.commands.model.metadata

interface MutableMetadata {
    operator fun <T : Any> set(key: Metadata.Key<T>, value: T)

    operator fun <T : Any> get(key: Metadata.Key<T>): T?

    fun remove(key: Metadata.Key<*>)

    fun toMetaData(): Metadata

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

            override fun toMetaData(): Metadata {
                return if (map.isEmpty()) EmptyMetaData
                else Metadata.from(map.toMap())
            }
        }
    }
}


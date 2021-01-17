package dev.kord.x.commands.model.metadata

/**
 * A mutable version of [Metadata], allowing you to [set] keys and value.
 */
interface MutableMetadata {

    /**
     * Pairs the [key] to the [value], allowing retrieval via [get].
     * Existing values under [key] will be overridden.
     */
    operator fun <T : Any> set(key: Metadata.Key<T>, value: T)

    /**
     * Gets the value paired to [key], or null if value was assigned to this key.
     */
    operator fun <T : Any> get(key: Metadata.Key<T>): T?

    /**
     * Removes the value assigned to this key if it exists.
     */
    fun remove(key: Metadata.Key<*>)

    /**
     * Returns a [Metadata] with the currently existing key-value pairs.
     */
    fun toMetaData(): Metadata

    companion object {

        /**
         * Creates a [MutableMetadata] that optimizes for empty metadata.
         */
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


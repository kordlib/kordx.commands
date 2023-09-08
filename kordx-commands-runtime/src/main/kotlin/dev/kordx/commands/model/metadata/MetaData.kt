package dev.kordx.commands.model.metadata

/**
 * Key-value container used to store command or module related data.
 */
interface Metadata {

    /**
     * Type token to retrieve a metadata value.
     */
    interface Key<T : Any>

    /**
     * Gets the value bound to this key, if present.
     */
    operator fun <T : Any> get(key: Key<T>): T?

    companion object {

        /**
         * Creates a [Metadata] backed by a [map].
         */
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

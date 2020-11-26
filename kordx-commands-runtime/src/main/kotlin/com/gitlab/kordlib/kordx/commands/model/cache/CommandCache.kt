package com.gitlab.kordlib.kordx.commands.model.cache

/**
 * Command cache, used for data exchange between first/prior run and current run if the command is edited.
 */
interface CommandCache {
    /**
     * Retrieves the value if stored previously, or null.
     */
    fun <T> getOrNull(): T?

    /**
     * Clears the value (if stored previously).
     */
    fun clear()

    /**
     * Sets or updates the value of Cache for the particular command
     */
    fun setOrUpdate(value: Any)


    companion object {
        /**
         * A disabled cache, which has nothing
         */
        fun disabled(): CommandCache = ConstantDisabledCache
    }
}

@Suppress("EmptyFunctionBlock")
private object ConstantDisabledCache : CommandCache {
    override fun <T> getOrNull(): T? = null
    override fun clear() {}
    override fun setOrUpdate(value: Any) {}
}

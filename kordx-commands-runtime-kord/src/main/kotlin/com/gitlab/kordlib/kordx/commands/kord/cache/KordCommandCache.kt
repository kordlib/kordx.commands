package com.gitlab.kordlib.kordx.commands.kord.cache

import com.gitlab.kordlib.cache.api.data.description
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.model.cache.CommandCache


/**
 * Command Cache for [Kord] events.
 *
 * @param kord Kord instance.
 *
 * @param messageId id of the event's message used as an identifier for this cache.
 */
class KordCommandCache(
        val kord: Kord,
        val messageId: Long,
) : CommandCache {
    private var value: Any? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T> getOrNull(): T? = value as? T

    override fun clear() {
        value = null
    }

    override fun setOrUpdate(value: Any) {
        this.value = value
    }

    companion object {
        /**
         * Description for storing this as a Kord's [com.gitlab.kordlib.cache.api.DataCache]
         */
        val description = description(KordCommandCache::messageId)
    }
}

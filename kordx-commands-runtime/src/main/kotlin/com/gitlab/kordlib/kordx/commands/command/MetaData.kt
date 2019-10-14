package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument

interface MetaData {
    interface Key<T: Any>

    operator fun<T: Any> get(key: Key<T>): T?

}

interface MutableMetaData: MetaData {
    operator fun<T: Any> set(key: MetaData.Key<T>, value: T)

    fun remove(key: MetaData.Key<*>)

    companion object {
        operator fun invoke() = object : MutableMetaData {
            private val map = mutableMapOf<MetaData.Key<*>, Any>()

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> get(key: MetaData.Key<T>): T? {
                return map[key] as? T?
            }

            override fun <T : Any> set(key: MetaData.Key<T>, value: T) {
                map[key] = value
            }

            override fun remove(key: MetaData.Key<*>) {
                map.remove(key)
            }
        }
    }
}

typealias CommandExecution<T> = suspend (T, List<*>) -> Unit
object Execution: MetaData.Key<CommandExecution<*>>
object Arguments: MetaData.Key<List<Argument<*,*>>>


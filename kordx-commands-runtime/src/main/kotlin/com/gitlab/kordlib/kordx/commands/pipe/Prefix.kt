package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.CommonContext

typealias PrefixSupplier<S> = suspend (S) -> String

class Prefix(private val suppliers: Map<CommandContext<*, *, *>, PrefixSupplier<*>> = mapOf()) {

    fun contains(context: CommandContext<*, *, *>): Boolean = context in suppliers

    @Suppress("UNCHECKED_CAST")
    suspend fun <S, A, E> getPrefix(context: CommandContext<S, A, E>, event: S): String {
        val supplier = (suppliers[context] ?: suppliers[CommonContext] ?: return "") as PrefixSupplier<S>
        return supplier(event)
    }

}

class PrefixBuilder(val suppliers: MutableMap<CommandContext<*, *, *>, PrefixSupplier<*>> = mutableMapOf()) {

    fun <S, A, E> add(context: CommandContext<S, A, E>, supplier: suspend (S) -> String) {
        suppliers[context] = supplier
    }

    fun build(): Prefix = Prefix(suppliers.toMap())

}

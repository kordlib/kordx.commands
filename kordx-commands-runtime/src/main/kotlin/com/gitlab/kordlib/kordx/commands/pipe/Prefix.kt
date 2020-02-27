package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.CommonContext
import com.gitlab.kordlib.kordx.commands.command.PipeContext

typealias PrefixSupplier<S> = suspend (S) -> String

fun prefix(configuration: PrefixBuilder.() -> Unit) = object : PrefixConfiguration {
    override fun apply(builder: PrefixBuilder) = builder.configuration()
}

interface PrefixConfiguration {

    fun apply(builder: PrefixBuilder)

}

class Prefix(private val suppliers: Map<PipeContext<*, *, *>, PrefixSupplier<*>> = mapOf()) {

    fun contains(context: PipeContext<*, *, *>): Boolean = context in suppliers

    @Suppress("UNCHECKED_CAST")
    suspend fun <S, A, E : CommandContext> getPrefix(context: PipeContext<S, A, E>, event: S): String {
        val supplier = (suppliers[context] ?: suppliers[CommonContext] ?: return "") as PrefixSupplier<S>
        return supplier(event)
    }

}

class PrefixBuilder(val suppliers: MutableMap<PipeContext<*, *, *>, PrefixSupplier<*>> = mutableMapOf()) {

    fun <S, A, E : CommandContext> add(context: PipeContext<S, A, E>, supplier: suspend (S) -> String) {
        suppliers[context] = supplier
    }

    fun build(): Prefix = Prefix(suppliers.toMap())

}

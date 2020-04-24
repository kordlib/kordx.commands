package com.gitlab.kordlib.kordx.commands.model.prefix

import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

fun prefix(configuration: PrefixBuilder.() -> Unit) = object : PrefixConfiguration {
    override fun apply(builder: PrefixBuilder) = builder.configuration()
}

class Prefix(private val suppliers: Map<ProcessorContext<*, *, *>, PrefixSupplier<*>> = mapOf()) {

    fun contains(context: ProcessorContext<*, *, *>): Boolean = context in suppliers

    @Suppress("UNCHECKED_CAST")
    suspend fun <S, A, E : CommandContext> getPrefix(context: ProcessorContext<S, A, E>, event: S): String {
        val supplier = (suppliers[context] ?: suppliers[CommonContext] ?: return "") as PrefixSupplier<S>
        return supplier(event)
    }

}

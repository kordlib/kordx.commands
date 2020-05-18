package com.gitlab.kordlib.kordx.commands.model.prefix

import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

/**
 * Creates a [PrefixConfiguration] from the [configuration].
 */
fun prefix(configuration: PrefixBuilder.() -> Unit) = object : PrefixConfiguration {
    override fun apply(builder: PrefixBuilder) = builder.configuration()
}

/**
 * A collection of [suppliers] used to get a prefix for a certain [ProcessorContext].
 */
class Prefix(private val suppliers: Map<ProcessorContext<*, *, *>, PrefixSupplier<*>> = mapOf()) {

    /**
     * Returns the most precise prefix for the [context]. First trying to find one for the [context], then
     * for the [CommonContext] and finally returning an empty String to denote a lock of prefix.
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun <S, A, E : CommandEvent> getPrefix(context: ProcessorContext<S, A, E>, event: S): String {
        val supplier = (suppliers[context] ?: suppliers[CommonContext] ?: return "") as PrefixSupplier<S>
        return supplier(event)
    }

}

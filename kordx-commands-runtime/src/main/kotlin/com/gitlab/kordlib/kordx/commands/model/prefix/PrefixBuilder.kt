package com.gitlab.kordlib.kordx.commands.model.prefix

import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

/**
 * Builder for the [Prefix] instance of a [CommandProcessor].
 *
 * @param suppliers PrefixSuppliers mapped to their context.
 */
class PrefixBuilder(private val suppliers: MutableMap<ProcessorContext<*, *, *>, PrefixSupplier<*>> = mutableMapOf()) {

    /**
     * Adds the [supplier] for the [context], replacing the previous supplier for that context if present.
     */
    fun <S, A, E : CommandEvent> add(context: ProcessorContext<S, A, E>, supplier: suspend (S) -> String) {
        suppliers[context] = supplier
    }

    /**
     * Removes the supplier for the [context] if present.
     */
    fun <S, A, E : CommandEvent> remove(context: ProcessorContext<S, A, E>) {
        suppliers.remove(context)
    }

    /**
     * Gets the prefix supplier registered to this [context], if present.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <S, A, E : CommandEvent> get(context: ProcessorContext<S, A, E>): PrefixSupplier<S>? {
        return suppliers[context] as? PrefixSupplier<S>
    }

    /**
     * Builds a prefix with the current suppliers.
     */
    fun build(): Prefix = Prefix(suppliers.toMap())

}

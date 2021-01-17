package dev.kord.x.commands.model.prefix

import dev.kord.x.commands.model.command.CommandEvent
import dev.kord.x.commands.model.processor.CommandProcessor
import dev.kord.x.commands.model.processor.ProcessorContext
import org.koin.core.Koin
import org.koin.core.KoinComponent

/**
 * Builder for the [Prefix] instance of a [CommandProcessor].
 *
 * @param suppliers PrefixSuppliers mapped to their context.
 */
class PrefixBuilder(
    private val koin: Koin,
    private val suppliers: MutableMap<ProcessorContext<*, *, *>, PrefixRule<*>> = mutableMapOf()
) : KoinComponent {

    override fun getKoin(): Koin = koin

    /**
     * Adds the [supplier] for the [context], replacing the previous supplier for that context if present.
     */
    fun <S, A, E : CommandEvent> add(
        context: ProcessorContext<S, A, E>,
        supplier: () -> PrefixRule<S>
    ) {
        suppliers[context] = supplier()
    }

    /**
     * Adds the [supplier] for the [context], replacing the previous supplier for that context if present.
     */
    fun <S, A, E : CommandEvent> add(context: ProcessorContext<S, A, E>, supplier: PrefixRule<S>) {
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
    operator fun <S, A, E : CommandEvent> get(context: ProcessorContext<S, A, E>): PrefixRule<S>? {
        return suppliers[context] as? PrefixRule<S>
    }

    /**
     * Builds a prefix with the current suppliers.
     */
    fun build(): Prefix = Prefix(suppliers.toMap())

}

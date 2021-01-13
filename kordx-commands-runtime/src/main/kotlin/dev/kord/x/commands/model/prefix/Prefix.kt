package dev.kord.x.commands.model.prefix

import dev.kord.x.commands.model.command.CommandEvent
import dev.kord.x.commands.model.context.CommonContext
import dev.kord.x.commands.model.processor.ProcessorContext

/**
 * Creates a [PrefixConfiguration] from the [configuration].
 */
fun prefix(configuration: PrefixBuilder.() -> Unit): PrefixConfiguration =
    object : PrefixConfiguration {
        override fun apply(builder: PrefixBuilder) = builder.configuration()
    }

/**
 * A collection of [suppliers] used to get a prefix for a certain [ProcessorContext].
 */
class Prefix(private val suppliers: Map<ProcessorContext<*, *, *>, PrefixRule<*>> = mapOf()) {

    /**
     * Returns the most precise [PrefixRule] for the [context]. First trying to find one for the [context], then
     * for the [CommonContext]. Returns null if neither contain a rule.
     */
    @Suppress("UNCHECKED_CAST")
    fun <S, A, E : CommandEvent> getPrefixRule(
        context: ProcessorContext<S, A, E>
    ): PrefixRule<S>? = (suppliers[context] ?: suppliers[CommonContext]) as? PrefixRule<S>

}

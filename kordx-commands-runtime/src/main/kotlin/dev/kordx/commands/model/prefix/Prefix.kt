package dev.kordx.commands.model.prefix

import dev.kordx.commands.model.command.CommandEvent
import dev.kordx.commands.model.context.CommonContext
import dev.kordx.commands.model.processor.ProcessorContext

/**
 * Creates a [PrefixConfiguration] from the [configuration].
 */
fun prefix(configuration: PrefixBuilder.() -> Unit) = object : PrefixConfiguration {
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

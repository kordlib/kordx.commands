package dev.kordx.commands.model.plug

import dev.kordx.commands.model.processor.ProcessorBuilder
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Container for all registered [plugs] in a [ProcessorBuilder].
 */
class PlugContainer(@PublishedApi internal val plugs: Map<KType, List<Plug>>) {

    /**
     * Gets all plugs related stored under the type token [T].
     */
    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T: Plug> getPlugs(): List<T> = (plugs[typeOf<T>()] as? List<T>).orEmpty()

}

package dev.kordx.commands.model.plug

import dev.kordx.commands.model.processor.ProcessorBuilder

/**
 * Handler for plugs. Implementations can be registered (or autowired) in the [ProcessorBuilder] and will be
 * called on [ProcessorBuilder.build].
 */
interface PlugSocket {

    /**
     * Handles the [container] on [ProcessorBuilder.build], processing the required [plugs][PlugContainer.plugs].
     */
    suspend fun handle(container: PlugContainer)

}

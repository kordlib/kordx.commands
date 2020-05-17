package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter
import com.gitlab.kordlib.kordx.commands.model.metadata.EachCommandModifier
import com.gitlab.kordlib.kordx.commands.model.module.Module
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.plug.Plug
import com.gitlab.kordlib.kordx.commands.model.plug.PlugContainer
import com.gitlab.kordlib.kordx.commands.model.plug.PlugSocket
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.model.prefix.Prefix
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.dsl.koinApplication
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Builder for [CommandProcessor].
 */
open class ProcessorBuilder : KoinComponent {
    override fun getKoin(): Koin = koinApplication.koin

    /**
     * The koin application that will be shared across all entities, used for dependency injection
     * and autowired dependency resolving.
     */
    val koinApplication: KoinApplication = koinApplication { }

    /**
     * Event filters that will be applied to their appropriate [EventFilter.context].
     */
    val eventFilters: MutableList<EventFilter<*>> = mutableListOf()

    /**
     * Event handlers mapper to their context.
     * Each [ProcessorContext] requires exactly one [EventHandler] unless a handler for the [CommonContext] is present,
     * in which case other handlers become optional.
     */
    val eventHandlers: MutableMap<ProcessorContext<*, *, *>, EventHandler<*>> = mutableMapOf()

    /**
     * Event sources that spawn new events to be processed.
     */
    val eventSources: MutableList<EventSource<*>> = mutableListOf()

    /**
     * Preconditions that will be applied to their appropriate [Precondition.context].
     */
    val preconditions: MutableList<Precondition<*>> = mutableListOf()

    /**
     * Builder for the [Prefix].
     */
    val prefixBuilder: PrefixBuilder = PrefixBuilder()

    /**
     * Modifiers used to build [modules][Module] and [commands][Command]
     */
    val moduleModifiers: MutableList<ModuleModifier> = mutableListOf(EachCommandModifier)

    /**
     *
     * Plugs mapped to their type which will be processed on [build] by [plugSockets].
     */
    val plugs: MutableMap<KType, MutableList<Plug>> = mutableMapOf()

    /**
     * Plug sockets used to process [plugs].
     */
    val plugSockets: MutableList<PlugSocket> = mutableListOf()

    /**
     * Dispatcher for processing [eventSources], default is [Dispatchers.Default].
     */
    var dispatcher: CoroutineDispatcher = Dispatchers.Default

    /**
     * DSL to configure the [koinApplication].
     */
    inline fun koin(builder: KoinApplication.() -> Unit) {
        koinApplication.builder()
    }

    /**
     * DSL to configure the [PrefixBuilder].
     */
    inline fun prefix(builder: PrefixBuilder.() -> Unit) {
        prefixBuilder.builder()
    }

    /**
     * Adds the [plug] to the configuration under its type [T].
     */
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T : Plug> addPlug(plug: T) {
        plugs.getOrPut(typeOf<T>()) { mutableListOf() }.add(plug)
    }

    /**
     * Adds this plug to the configuration under its type [T].
     */
    inline fun <reified T : Plug> T.unaryPlus() = addPlug(this)

    /**
     * Adds the socket to the configuration.
     */
    operator fun PlugSocket.unaryPlus() {
        plugSockets.add(this)
    }

    /**
     * Adds the event handler to the configuration.
     */
    operator fun EventHandler<*>.unaryPlus() {
        eventHandlers[context] = this
    }

    /**
     * Adds the precondition to the configuration.
     */
    operator fun Precondition<*>.unaryPlus() {
        preconditions.add(this)
    }

    /**
     * Adds the event filter to the configuration.
     */
    operator fun EventFilter<*>.unaryPlus() {
        eventFilters.add(this)
    }

    /**
     * Adds the module modifier to the configuration.
     */
    operator fun ModuleModifier.unaryPlus() {
        moduleModifiers.add(this)
    }

    /**
     * Removes the event filter from the configuration.
     */
    operator fun EventFilter<*>.unaryMinus() {
        eventFilters.remove(this)
    }

    /**
     * Removes the module modifier from the configuration.
     */
    operator fun ModuleModifier.unaryMinus() {
        moduleModifiers.add(this)
    }

    /**
     * Builds a [CommandProcessor] based on the current properties of the builder, starting all event sources
     * and configures all [plugs][Plug] and [plug sockets][PlugSocket].
     */
    open suspend fun build(): CommandProcessor {
        val container = ModuleContainer()
        moduleModifiers.forEach { it.apply(container) }
        container.applyForEach()

        val modules: MutableMap<String, Module> = mutableMapOf()
        container.modules.values.forEach { it.build(modules, koinApplication.koin) }

        val data = CommandProcessorData(
                filters = eventFilters.groupBy { it.context },
                commands = modules.values.map { it.commands }.fold(emptyMap()) { acc, map -> acc + map },
                handlers = eventHandlers.toMap(),
                preconditions = preconditions.groupBy { it.context },
                prefix = prefixBuilder.build(),
                modifiers = moduleModifiers,
                dispatcher = dispatcher,
                koin = koinApplication.koin
        )

        val pipe = CommandProcessor(data)

        eventSources.map { pipe.addSource(it) }

        val plugContainer = PlugContainer(plugs)
        plugSockets.forEach { it.handle(plugContainer) }

        return pipe
    }

    companion object {

        /**
         * DSL for building a [CommandProcessor].
         */
        suspend inline operator fun invoke(builder: ProcessorBuilder.() -> Unit): CommandProcessor {
            return ProcessorBuilder().apply(builder).build()
        }

    }

}

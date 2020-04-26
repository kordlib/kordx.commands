package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter
import com.gitlab.kordlib.kordx.commands.model.metadata.EachCommandModifier
import com.gitlab.kordlib.kordx.commands.model.module.Module
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.plug.Plug
import com.gitlab.kordlib.kordx.commands.model.plug.PlugSocket
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.dsl.koinApplication

@Suppress("MemberVisibilityCanBePrivate")
open class ProcessorConfig : KoinComponent {
    override fun getKoin(): Koin = koinApplication.koin

    val koinApplication: KoinApplication = GlobalContext.getOrNull() ?: koinApplication { }
    val eventFilters: MutableList<EventFilter<*>> = mutableListOf()
    val eventHandlers: MutableMap<ProcessorContext<*, *, *>, EventHandler<*>> = mutableMapOf()
    val eventSources: MutableList<EventSource<*>> = mutableListOf()
    val preconditions: MutableList<Precondition<*>> = mutableListOf()
    val prefixBuilder: PrefixBuilder = PrefixBuilder()
    val moduleModifiers: MutableList<ModuleModifier> = mutableListOf(EachCommandModifier)
    val plugs: MutableMap<Plug.Key<*>, MutableList<Plug<*>>> = mutableMapOf<Plug.Key<*>, MutableList<Plug<*>>>()
    val plugSockets: MutableMap<Plug.Key<*>, PlugSocket<*, *>> = mutableMapOf()
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    inline fun koin(builder: KoinApplication.() -> Unit) {
        koinApplication.builder()
    }

    inline fun prefix(builder: PrefixBuilder.() -> Unit) {
        prefixBuilder.builder()
    }

    fun addPlugs(plugs: List<Plug<*>>) {
        plugs.forEach { +it }
    }

    operator fun Plug<*>.unaryPlus() {
        plugs.getOrPut(key) { mutableListOf() }.add(this)
    }

    operator fun PlugSocket<*, *>.unaryPlus() {
        plugSockets[key] = this
    }

    operator fun EventHandler<*>.unaryPlus() {
        eventHandlers[context] = this
    }

    operator fun Precondition<*>.unaryPlus() {
        preconditions.add(this)
    }

    operator fun EventFilter<*>.unaryPlus() {
        eventFilters.add(this)
    }

    operator fun ModuleModifier.unaryPlus() {
        moduleModifiers.add(this)
    }

    operator fun EventFilter<*>.unaryMinus() {
        eventFilters.remove(this)
    }

    operator fun ModuleModifier.unaryMinus() {
        moduleModifiers.add(this)
    }

    open suspend fun build(): CommandProcessor {
        val container = ModuleContainer()
        moduleModifiers.forEach { it.apply(container) }
        container.applyForEach()

        val modules: MutableMap<String, Module> = mutableMapOf()
        container.modules.values.forEach { it.build(modules, koinApplication.koin) }

        val pipe = CommandProcessor(
                filters = eventFilters.groupBy { it.context },
                commands = modules.values.map { it.commands }.fold(emptyMap()) { acc, map -> acc + map },
                handlers = eventHandlers.toMap(),
                preconditions = preconditions.groupBy { it.context },
                prefix = prefixBuilder.build(),
                modifiers = moduleModifiers,
                dispatcher = dispatcher,
                koin = koinApplication.koin
        )

        this.eventSources.map { pipe.addSource(it) }
        plugs.forEach { (key, plugs) ->
            plugs.forEach { plugSockets.getValue(key).handle(listOf(it) as List<Nothing>) }
        }

        return pipe
    }

    companion object {
        suspend operator fun invoke(builder: ProcessorConfig.() -> Unit): CommandProcessor {
            return ProcessorConfig().apply(builder).build()
        }

    }

}

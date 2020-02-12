package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.Module
import com.gitlab.kordlib.kordx.commands.flow.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PipeConfig {
    val eventFilters: MutableList<EventFilter<*>> = mutableListOf()
    var eventHandler: EventHandler = DefaultHandler
    val eventSources: MutableList<EventSource<*>> = mutableListOf()
    val preconditions: MutableList<Precondition<*>> = mutableListOf()
    val prefixes: MutableList<Prefix<*, *, *>> = mutableListOf()
    val moduleModifiers: MutableList<ModuleModifier> = mutableListOf(EachCommandModifier)
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    operator fun Prefix<*, *, *>.unaryPlus() {
        prefixes += this
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

    suspend fun build(): Pipe {
        val container = ModuleContainer()
        moduleModifiers.forEach { it.apply(container) }
        container.applyForEach()

        val modules: MutableMap<String, Module> = mutableMapOf()
        container.modules.values.forEach { it.build(modules) }

        val filters = mutableMapOf<CommandContext<*, *, *>, MutableList<EventFilter<*>>>()
        eventFilters.forEach { filters.getOrDefault(it.context, mutableListOf()).add(it) }

        val pipe = Pipe(
                filters = filters,
                commands = modules.values.map { it.commands }.fold(mutableMapOf()) { acc, map -> acc += map; acc },
                handler = eventHandler,
                preconditions = preconditions.groupBy { it.context },
                prefixes = prefixes.map { it.context to it }.toMap(),
                modifiers = moduleModifiers,
                dispatcher = dispatcher
        )

        this.eventSources.map { pipe.add(it) }
        return pipe
    }

    companion object {
        suspend operator fun invoke(builder: PipeConfig.() -> Unit): Pipe {
            return PipeConfig().apply(builder).build()
        }

    }

}

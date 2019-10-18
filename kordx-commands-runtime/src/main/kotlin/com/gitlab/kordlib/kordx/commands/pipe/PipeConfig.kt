package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.Module
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.flow.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList

class PipeConfig {
    val eventFilters: MutableList<EventFilter<*>> = mutableListOf()
    var eventHandler: EventHandler = DefaultHandler
    val eventSources: MutableList<EventSource<*>> = mutableListOf()
    val preconditions: MutableList<Precondition<*>> = mutableListOf()
    val prefixes: MutableList<Prefix<*, *, *>> = mutableListOf()
    val moduleGenerators: MutableList<ModuleGenerator> = mutableListOf()
    val moduleModifiers: MutableList<ModuleModifier> = mutableListOf(
            EachCommandModifier
    )
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun build(): Pipe {
        val builders: List<ModuleBuilder<*, *, *>> = flow<ModuleBuilder<*, *, *>> {
            moduleGenerators.forEach { with(it) { generate() } }
        }.onEach { builder ->
            moduleModifiers.forEach { it.modify(builder) }
        }.toList()

        val modules: MutableMap<String, Module> = mutableMapOf()
        builders.forEach { it.build(modules) }

        val map = mutableMapOf<CommandContext<*, *, *>, MutableList<EventFilter<*>>>()
        eventFilters.forEach { map.getOrDefault(it.context, mutableListOf()).add(it) }

        val pipe = Pipe(
                filters = map,
                commands = modules.values.map { it.commands }.fold(mutableMapOf()) { acc, map -> acc += map; acc },
                handler = eventHandler,
                preconditions = preconditions.map { it.context to it }.toMap() as Map<CommandContext<*, *, *>, List<Precondition<*>>>,
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

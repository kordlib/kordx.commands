package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.Module
import com.gitlab.kordlib.kordx.commands.flow.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Suppress("MemberVisibilityCanBePrivate")
class PipeConfig {
    val eventFilters: MutableList<EventFilter<*>> = mutableListOf()
    val eventHandlers: MutableMap<CommandContext<*,*,*>, EventHandler<*>> = mutableMapOf()
    val eventSources: MutableList<EventSource<*>> = mutableListOf()
    val preconditions: MutableList<Precondition<*>> = mutableListOf()
    val prefixBuilder: PrefixBuilder = PrefixBuilder()
    val moduleModifiers: MutableList<ModuleModifier> = mutableListOf(EachCommandModifier)
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    inline fun prefix(builder: PrefixBuilder.() -> Unit) {
        prefixBuilder.builder()
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

        val pipe = Pipe(
                filters = eventFilters.groupBy { it.context },
                commands = modules.values.map { it.commands }.fold(mutableMapOf()) { acc, map -> acc += map; acc },
                handlers = eventHandlers.toMap(),
                preconditions = preconditions.groupBy { it.context },
                prefix = prefixBuilder.build(),
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

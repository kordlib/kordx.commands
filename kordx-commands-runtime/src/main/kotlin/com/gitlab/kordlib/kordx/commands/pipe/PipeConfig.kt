package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.PipeContext
import com.gitlab.kordlib.kordx.commands.command.Module
import com.gitlab.kordlib.kordx.commands.flow.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.dsl.koinApplication

@Suppress("MemberVisibilityCanBePrivate")
class PipeConfig: KoinComponent {
    override fun getKoin(): Koin = koinApplication.koin

    val koinApplication: KoinApplication = GlobalContext.getOrNull() ?: koinApplication {  }
    val eventFilters: MutableList<EventFilter<*>> = mutableListOf()
    val eventHandlers: MutableMap<PipeContext<*,*,*>, EventHandler<*>> = mutableMapOf()
    val eventSources: MutableList<EventSource<*>> = mutableListOf()
    val preconditions: MutableList<Precondition<*>> = mutableListOf()
    val prefixBuilder: PrefixBuilder = PrefixBuilder()
    val moduleModifiers: MutableList<ModuleModifier> = mutableListOf(EachCommandModifier)
    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    inline fun koin(builder: KoinApplication.() -> Unit) {
        koinApplication.builder()
    }

    inline fun prefix(builder: PrefixBuilder.() -> Unit) {
        prefixBuilder.builder()
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

    suspend fun build(): Pipe {
        val container = ModuleContainer()
        moduleModifiers.forEach { it.apply(container) }
        container.applyForEach()

        val modules: MutableMap<String, Module> = mutableMapOf()
        container.modules.values.forEach { it.build(modules) }

        val pipe = Pipe(
                filters = eventFilters.groupBy { it.context },
                commands = modules.values.map { it.commands }.fold(emptyMap()) { acc, map -> acc + map },
                handlers = eventHandlers.toMap(),
                preconditions = preconditions.groupBy { it.context },
                prefix = prefixBuilder.build(),
                modifiers = moduleModifiers,
                dispatcher = dispatcher,
                koin = koinApplication.koin
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

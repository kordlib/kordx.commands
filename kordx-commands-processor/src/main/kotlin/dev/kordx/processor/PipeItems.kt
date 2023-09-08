package dev.kordx.processor

import javax.lang.model.element.ExecutableElement

/**
 * Container for all autowired entities collected during processing.
 *
 * @param koins koin [modules][org.koin.core.module.Module]
 * @param modules kordx [module modifiers][dev.kordx.commands.model.module.ModuleModifier]
 * @param sources kordx [event sources][dev.kordx.commands.model.processor.EventSource]
 * @param filters kordx [event filters][dev.kordx.commands.model.eventFilter.EventFilter]
 * @param handlers kordx [event handlers][dev.kordx.commands.model.processor.EventHandler]
 * @param commandSets kordx [command sets][dev.kordx.commands.model.module.CommandSet]
 * @param preconditions kordx [preconditions][dev.kordx.commands.model.precondition.Precondition]
 * @param prefixes kordx [prefix configurations][dev.kordx.commands.model.prefix.PrefixConfiguration]
 * @param plugs kordx [plugs][dev.kordx.commands.model.plug.Plug]
 */
data class PipeItems(
        val koins: MutableSet<ExecutableElement> = mutableSetOf(),
        val modules: MutableSet<ExecutableElement> = mutableSetOf(),
        val sources: MutableSet<ExecutableElement> = mutableSetOf(),
        val filters: MutableSet<ExecutableElement> = mutableSetOf(),
        val handlers: MutableSet<ExecutableElement> = mutableSetOf(),
        val commandSets: MutableSet<ExecutableElement> = mutableSetOf(),
        val preconditions: MutableSet<ExecutableElement> = mutableSetOf(),
        val prefixes: MutableSet<ExecutableElement> = mutableSetOf(),
        val plugs: MutableSet<ExecutableElement> = mutableSetOf()
) {
    /**
     * Returns `true` if all autowired property lists are empty.
     */
    fun isEmpty() = listOf(
            koins,
            modules,
            sources,
            filters,
            handlers,
            commandSets,
            preconditions,
            prefixes,
            plugs
    ).all { it.isEmpty() }
}

package com.gitlab.kordlib.kordx.processor

import javax.lang.model.element.ExecutableElement

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
    fun isEmpty() = listOf(koins, modules, sources, filters, handlers, preconditions).all { it.isEmpty() }
}
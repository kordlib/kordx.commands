package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder

/**
 * A container for all module builders currently created, modules can be queried by name and will be created
 * if none match.
 */
class ModuleContainer(internal val modules: MutableMap<String, ModuleBuilder<*, *, *>> = mutableMapOf()) {

    private val consumers: MutableList<suspend (ModuleBuilder<*, *, *>) -> Unit> = mutableListOf()

    /**
     * Gets or creates a [ModuleBuilder] with the given [name].
     */
    operator fun get(name: String): ModuleBuilder<*, *, *> = modules.getOrPut(name) { ModuleBuilder(name, CommonContext) }

    /**
     * Removes a module with this name, if it exists.
     */
    operator fun String.unaryMinus() {
        modules.remove(this)
    }

    /**
     * Removes a module with the given [moduleName], if it exists.
     */
    fun remove(moduleName: String) {
        modules.remove(moduleName)
    }

    /**
     * Gets or creates the [ModuleBuilder] with the given [name] and applies to [consumer].
     */
    inline fun apply(name: String, consumer: (ModuleBuilder<*, *, *>) -> Unit) {
        get(name).apply(consumer)
    }

    /**
     * Applies the given [consumer] to every module that exists at the end of module processing.
     */
    fun forEach(consumer: suspend (ModuleBuilder<*, *, *>) -> Unit) {
        consumers += consumer
    }

    internal suspend fun applyForEach() {
        modules.values.forEach { builder -> consumers.forEach { consumer -> consumer(builder) } }
    }

}
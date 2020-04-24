package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder

class ModuleContainer(internal val modules: MutableMap<String, ModuleBuilder<*, *, *>> = mutableMapOf()) {

    private val consumers: MutableList<suspend  (ModuleBuilder<*, *, *>) -> Unit> = mutableListOf()

    operator fun get(name: String): ModuleBuilder<*, *, *> = modules.getOrPut(name) { ModuleBuilder(name, CommonContext) }

    operator fun String.unaryMinus() {
        modules.remove(this)
    }

    fun remove(module: String) {
        modules.remove(module)
    }

    inline fun apply(name: String, consumer: (ModuleBuilder<*, *, *>) -> Unit) {
        get(name).apply(consumer)
    }

    fun forEach(consumer: suspend (ModuleBuilder<*, *, *>) -> Unit) {
        consumers += consumer
    }

    internal suspend fun applyForEach() {
        modules.values.forEach { builder -> consumers.forEach { consumer -> consumer(builder) } }
    }

}
package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.CommandSet
import com.gitlab.kordlib.kordx.commands.command.EachCommand
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder

interface ModuleModifier {

    suspend fun apply(container: ModuleContainer)

    companion object {
        fun from(moduleName: String, set: CommandSet) = object : ModuleModifier {

            override suspend fun apply(container: ModuleContainer) {
                container.apply(moduleName) {
                    with(set) { it.apply() }
                }
            }
        }
    }
}

inline fun moduleModifier(crossinline block: suspend ModuleBuilder<*, *, *>.() -> Unit): ModuleModifier = object : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.forEach { it.block() }
}

inline fun moduleModifier(name: String, crossinline block: suspend ModuleBuilder<*, *, *>.() -> Unit): ModuleModifier = object : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.apply(name) { it.block() }
}

fun CommandSet.toModifier(forModuleName: String) = ModuleModifier.from(forModuleName, this)

object EachCommandModifier : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.forEach {
        val function = it.metaData[EachCommand] ?: return@forEach
        it.commands.values.forEach { it.apply(function) }
    }
}

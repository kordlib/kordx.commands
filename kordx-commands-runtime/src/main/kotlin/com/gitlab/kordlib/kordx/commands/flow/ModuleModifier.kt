package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.CommandSet
import com.gitlab.kordlib.kordx.commands.command.EachCommand
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder

interface ModuleModifier {

    suspend fun modify(builder: ModuleBuilder<*, *, *>)

    companion object {
        fun from(moduleName: String, set: CommandSet) = object : ModuleModifier {

            override suspend fun modify(builder: ModuleBuilder<*, *, *>) {
                if (builder.name == moduleName) {
                    with(set) { builder.apply() }
                }
            }
        }
    }
}

fun moduleModifier(block: ModuleBuilder<*, *, *>.() -> Unit): ModuleModifier = object : ModuleModifier {
    override suspend fun modify(builder: ModuleBuilder<*, *, *>) {
        builder.block()
    }
}

fun moduleModifier(name: String, block: ModuleBuilder<*, *, *>.() -> Unit): ModuleModifier = object : ModuleModifier {
    override suspend fun modify(builder: ModuleBuilder<*, *, *>) {
        if (builder.name == name) builder.block()
    }
}

fun CommandSet.toModifier(forModuleName: String) = ModuleModifier.from(forModuleName, this)

object EachCommandModifier : ModuleModifier {
    override suspend fun modify(builder: ModuleBuilder<*, *, *>) {
        val function = builder.metaData[EachCommand] ?: return
        builder.commands.values.forEach { it.apply(function) }
    }
}
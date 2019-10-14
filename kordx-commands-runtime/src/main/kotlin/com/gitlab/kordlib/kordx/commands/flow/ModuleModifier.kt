package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.command.dsl.CommandSet
import com.gitlab.kordlib.kordx.commands.command.dsl.EachCommand

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

fun CommandSet.toModifier(forModuleName: String) = ModuleModifier.from(forModuleName, this)

object EachCommandModifier : ModuleModifier {
    override suspend fun modify(builder: ModuleBuilder<*, *, *>) {
        val function = builder.metaData[EachCommand] ?: return
        builder.commands.forEach { it.apply(function) }
    }
}
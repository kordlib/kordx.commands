package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import kotlinx.coroutines.flow.FlowCollector

interface ModuleGenerator {

    suspend fun FlowCollector<ModuleBuilder<*, *, *>>.generate()

    companion object {

        fun from(builder: ModuleBuilder<*, *, *>) = object : ModuleGenerator {
            override suspend fun FlowCollector<ModuleBuilder<*, *, *>>.generate() = emit(builder)
        }

    }
}

fun ModuleBuilder<*,*,*>.toGenerator() = ModuleGenerator.from(this)
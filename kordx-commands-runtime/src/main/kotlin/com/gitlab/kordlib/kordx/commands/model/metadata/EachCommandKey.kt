package com.gitlab.kordlib.kordx.commands.model.metadata

import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandContext
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.processor.ModuleContainer

 object EachCommandKey : Metadata.Key<CommandBuilder<*, *, *>.() -> Unit>

object EachCommandModifier : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.forEach {
        val function = it.metaData[EachCommandKey] ?: return@forEach
        it.commands.values.forEach { it.apply(function) }
    }
}

fun <S, A, C: CommandContext> ModuleBuilder<S, A, C>.eachCommand(
        builder: CommandBuilder<*, *, *>.() -> Unit
) {
    when (val previous = metaData[EachCommandKey]) {
        null -> metaData[EachCommandKey] = builder
        else -> metaData[EachCommandKey] = {
            previous()
            builder()
        }
    }
}

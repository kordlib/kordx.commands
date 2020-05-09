package com.gitlab.kordlib.kordx.commands.model.metadata

import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.processor.ModuleContainer

 object EachCommandKey : Metadata.Key<CommandBuilder<*, *, *>.() -> Unit>

object EachCommandModifier : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.forEach {
        val function = it.metaData[EachCommandKey] ?: return@forEach
        it.commands.values.forEach { it.apply(function) }
        it.metaData.remove(EachCommandKey) //clean up after we're done
    }
}

/**
 * Defines a [configure] that will be applied to every command in this module **on module build**.
 */
fun <S, A, C: CommandEvent> ModuleBuilder<S, A, C>.eachCommand(
        configure: CommandBuilder<*, *, *>.() -> Unit
) {
    when (val previous = metaData[EachCommandKey]) {
        null -> metaData[EachCommandKey] = configure
        else -> metaData[EachCommandKey] = {
            previous()
            configure()
        }
    }
}

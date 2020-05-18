package com.gitlab.kordlib.kordx.commands.model.metadata

import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder
import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.module.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.processor.ModuleContainer
import com.gitlab.kordlib.kordx.commands.model.module.Module

/**
 * Type token for [EachCommandModifier].
 */
object EachCommandKey : Metadata.Key<CommandBuilder<*, *, *>.() -> Unit>

/**
 * A [ModuleModifier] that applies the lambda stored under [EachCommandKey] in the metadata of [Module] to each of
 * its commands.
 */
object EachCommandModifier : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.forEach {
        val function = it.metadata[EachCommandKey] ?: return@forEach
        it.commands.values.forEach { it.apply(function) }
        it.metadata.remove(EachCommandKey) //clean up after we're done
    }
}

/**
 * Defines a [configure] that will be applied to every command in this module **on module build**.
 */
fun <S, A, C : CommandEvent> ModuleBuilder<S, A, C>.eachCommand(
        configure: CommandBuilder<*, *, *>.() -> Unit
) {
    when (val previous = metadata[EachCommandKey]) {
        null -> metadata[EachCommandKey] = configure
        else -> metadata[EachCommandKey] = {
            previous()
            configure()
        }
    }
}

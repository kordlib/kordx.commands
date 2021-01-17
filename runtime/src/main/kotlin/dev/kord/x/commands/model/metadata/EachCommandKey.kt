package dev.kord.x.commands.model.metadata

import dev.kord.x.commands.model.command.CommandBuilder
import dev.kord.x.commands.model.command.CommandEvent
import dev.kord.x.commands.model.module.ModuleBuilder
import dev.kord.x.commands.model.module.ModuleModifier
import dev.kord.x.commands.model.processor.ModuleContainer

/**
 * Type token for [EachCommandModifier].
 */
object EachCommandKey : Metadata.Key<CommandBuilder<*, *, *>.() -> Unit>

/**
 * A [ModuleModifier] that applies the lambda stored under [EachCommandKey] in the metadata of [Module] to each of
 * its commands.
 */
object EachCommandModifier : ModuleModifier {
    override suspend fun apply(container: ModuleContainer): Unit = container.forEach { module ->
        val function = module.metadata[EachCommandKey] ?: return@forEach
        module.commands.values.forEach { it.apply(function) }
        module.metadata.remove(EachCommandKey) //clean up after we're done
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

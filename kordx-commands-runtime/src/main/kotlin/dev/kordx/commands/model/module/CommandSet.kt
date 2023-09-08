package dev.kordx.commands.model.module

import dev.kordx.commands.model.command.CommandBuilder
import dev.kordx.commands.model.command.CommandEvent
import dev.kordx.commands.model.processor.ProcessorContext

/**
 * A configuration of a [Module] that doesn't belong to any given module name.
 * The use case of CommandSets is small since module configurations are additive,
 * therefore the [module] builder is generally preferred over this one.
 *
 * ```kotlin
 * //don't do this
 * val set = commandSet(context) {
 *      //commands
 * }
 *
 * val module = module("name", context) {
 *     +set
 *     //commands
 * }
 *
 * //prefer this instead
 * val partialModule1 = module("name",context) {
 *      //commands
 * }
 *
 * val partialModule2 = module("name", context) {
 *     //commands
 * }
 * ```
 */
interface CommandSet {

    /**
     * Applies this CommandSet to the ModuleBuilder.
     */
    fun ModuleBuilder<*, *, *>.apply()
}

/**
 * Defines a [CommandSet] configured by the [builder].
 */
fun <S, A, C: CommandEvent> commands(
        context: ProcessorContext<S, A, C>,
        builder: ModuleBuilder<S, A, C>.() -> Unit
): CommandSet = object : CommandSet {
    override fun ModuleBuilder<*, *, *>.apply() {
        withContext(context) { builder() }
    }
}

/**
* Defines a [CommandSet] with a single command with the given [name] and configured by the [builder].
*/
fun <S, A, C: CommandEvent> command(
    context: ProcessorContext<S, A, C>,
    name: String,
    builder: CommandBuilder<S, A, C>.() -> Unit
): CommandSet = commands(context) { command(name, builder) }


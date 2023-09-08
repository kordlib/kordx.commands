package dev.kordx.commands.model.module

import dev.kordx.commands.model.processor.ModuleContainer

/**
 * Modifier that [applies][apply] changes to modules from a [ModuleContainer].
 */
interface ModuleModifier {

    /**
     * Applies changes to modules in the [container].
     */
    suspend fun apply(container: ModuleContainer)

    companion object {

        /**
         * Creates a [ModuleModifier] that applies the [set] to a module of the given [moduleName].
         */
        fun from(moduleName: String, set: CommandSet): ModuleModifier = object : ModuleModifier {

            override suspend fun apply(container: ModuleContainer) {
                container.apply(moduleName) {
                    with(set) { it.apply() }
                }
            }
        }
    }
}

/**
 * Creates a [ModuleModifier] that applies the [modification] to every [ModuleBuilder] in a [ModuleContainer].
 * This [modification] will be called **after** all modules are generated.
 */
inline fun forEachModule(
        crossinline modification: suspend ModuleBuilder<*, *, *>.() -> Unit
): ModuleModifier = object : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.forEach { it.modification() }
}

/**
 * Creates a modifier for a module with a given [name] that applies the [modification] to it.
 */
inline fun moduleModifier(
        name: String,
        crossinline modification: suspend ModuleBuilder<*, *, *>.() -> Unit
): ModuleModifier = object : ModuleModifier {
    override suspend fun apply(container: ModuleContainer) = container.apply(name) { it.modification() }
}

/**
 * Transforms a CommandSet into a modifier for a module with the given [name] that applies the CommandSet to it.
 */
fun CommandSet.toModifier(name: String) = ModuleModifier.from(name, this)


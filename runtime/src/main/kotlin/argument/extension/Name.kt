package dev.kord.x.commands.argument.extension

import dev.kord.x.commands.argument.Argument

/**
 * Returns a copy of this argument with the given [name].
 */
fun <T, CONTEXT> Argument<T, CONTEXT>.named(name: String) = object : Argument<T, CONTEXT> by this {
    override val name: String
        get() = name
}

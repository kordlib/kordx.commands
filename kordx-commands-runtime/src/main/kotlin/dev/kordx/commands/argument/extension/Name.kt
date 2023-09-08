package dev.kordx.commands.argument.extension

import dev.kordx.commands.argument.Argument

/**
 * Returns a copy of this argument with the given [name].
 */
fun <T, CONTEXT> dev.kordx.commands.argument.Argument<T, CONTEXT>.named(name: String) = object : dev.kordx.commands.argument.Argument<T, CONTEXT> by this {
    override val name: String
        get() = name
}

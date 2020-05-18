package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.Argument

/**
 * Returns an Argument that generates examples from the given [example] instead.
 */
fun <T, CONTEXT> Argument<T, CONTEXT>.example(
        example: () -> String
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override val example: String
        get() = example()
}

package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.Argument

fun <T, CONTEXT> Argument<T, CONTEXT>.named(name: String) = object : Argument<T, CONTEXT> by this {
    override val name: String
        get() = name
}
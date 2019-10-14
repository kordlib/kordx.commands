package com.gitlab.kordlib.kordx.commands.event

import com.gitlab.kordlib.kordx.commands.argument.Argument

interface ArgumentCollection<T, in CONTEXT> {
    val arguments: List<Argument<*, CONTEXT>>

    fun bundle(arguments: List<*>): T
}
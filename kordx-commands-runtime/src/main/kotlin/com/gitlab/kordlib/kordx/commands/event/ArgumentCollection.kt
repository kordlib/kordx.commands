package com.gitlab.kordlib.kordx.commands.event

import com.gitlab.kordlib.kordx.commands.argument.Argument

interface ArgumentCollection<T> {
    val arguments: List<Argument<*>>

    fun bundle(arguments: List<*>) : T
}
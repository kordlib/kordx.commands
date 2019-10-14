package com.gitlab.kordlib.kordx.commands.event

import com.gitlab.kordlib.kordx.commands.argument.Argument

data class SingleArg<T>(val first: T)

class SingleArgCollection<T, in CONTEXT>(private val argument: Argument<T, CONTEXT>) : ArgumentCollection<SingleArg<T>, CONTEXT> {
    override val arguments: List<Argument<*, CONTEXT>>
        get() = listOf(argument)

    override fun bundle(arguments: List<*>): SingleArg<T> = SingleArg(arguments[0]) as SingleArg<T>
}

fun <T, CONTEXT> args(argument: Argument<T, CONTEXT>) = SingleArgCollection(argument)

data class DoubleArg<A, B>(val first: A, val second: B)

class DoubleArgCollection<A, B, in CONTEXT>(private val first: Argument<A, CONTEXT>, private val second: Argument<B, CONTEXT>) : ArgumentCollection<DoubleArg<A, B>, CONTEXT> {
    override val arguments: List<Argument<*, CONTEXT>>
        get() = listOf(first, second)

    override fun bundle(arguments: List<*>): DoubleArg<A, B> = DoubleArg(arguments[0], arguments[1]) as DoubleArg<A, B>
}

fun <A, B, CONTEXT> args(first: Argument<A, CONTEXT>, second: Argument<B, CONTEXT>) = DoubleArgCollection(first, second)

package com.gitlab.kordlib.kordx.commands.argument.util

import com.gitlab.kordlib.kordx.commands.argument.*

infix fun<A,B, CONTEXT> Argument<A, CONTEXT>.or(other: Argument<B, CONTEXT>) = EitherArgument(this, other)

sealed class Either<A, B> {

    open val left: A?
        get() = when (this) {
            is Left -> value
            else -> null
        }

    open val right: B?
        get() = when (this) {
            is Right -> value
            else -> null
        }

    @JvmName("getLeft")
    operator fun get(ignored: Argument<A, *>): A? = left

    @JvmName("getRight")
    operator fun get(ignored: Argument<B, *>): B? = right
    data class Left<A, B>(val value: A) : Either<A, B>() {

        override val left: A
            get() = value
        override val right: Nothing
            get() = error("Left does not contain a right value")

    }
    data class Right<A, B>(val value: B) : Either<A, B>() {

        override val left: Nothing
            get() = error("Right does not contain a left value")
        override val right: B
            get() = value
    }

}

class EitherArgument<A, B, CONTEXT>(private val left: Argument<A, CONTEXT>, private val right: Argument<B, CONTEXT>, override val name: String = "Either") : Argument<Either<A, B>, CONTEXT> {
    override val example: String
        get() = "$left or $right"

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<Either<A, B>> {
        val left: Result<Either<A, B>> = left.parse(words, fromIndex, context).map { Either.Left<A, B>(it) }
        return left.switchOnFail { right.parse(words, fromIndex, context).map { Either.Right<A, B>(it) } }
    }

}

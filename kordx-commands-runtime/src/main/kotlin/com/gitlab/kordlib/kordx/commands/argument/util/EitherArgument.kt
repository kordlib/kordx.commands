package com.gitlab.kordlib.kordx.commands.argument.util

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.map
import com.gitlab.kordlib.kordx.commands.argument.switchOnFail

infix fun <A, B, CONTEXT> Argument<A, CONTEXT>.or(other: Argument<B, CONTEXT>) = EitherArgument(this, other)

sealed class Either<A, B> {

    operator fun component0(): A? = left
    operator fun component1(): B? = right

    @JvmName("getLeft")
    operator fun get(ignored: Argument<A, *>): A? = left

    @JvmName("getRight")
    operator fun get(ignored: Argument<B, *>): B? = right

    class Left<A, B>(val left: A) : Either<A, B>() {

        val right: Nothing
            get() = error("Left does not contain a right value")

    }

    class Right<A, B>(val right: B) : Either<A, B>() {

        val left: Nothing
            get() = error("Right does not contain a left value")
    }

}

val <A, B> Either<A, B>.left: A?
    get() = when (this) {
        is Either.Left -> left
        else -> null
    }

val <A, B> Either<A, B>.right: B?
    get() = when (this) {
        is Either.Right -> right
        else -> null
    }

class EitherArgument<A, B, CONTEXT>(private val left: Argument<A, CONTEXT>, private val right: Argument<B, CONTEXT>, override val name: String = "Either") : Argument<Either<A, B>, CONTEXT> {
    override val example: String
        get() = "$left or $right"

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<Either<A, B>> {
        val left: Result<Either<A, B>> = left.parse(words, fromIndex, context).map { Either.Left<A, B>(it) }
        return left.switchOnFail { right.parse(words, fromIndex, context).map { Either.Right<A, B>(it) } }
    }

}

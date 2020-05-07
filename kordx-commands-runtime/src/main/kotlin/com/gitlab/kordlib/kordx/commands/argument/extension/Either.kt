package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.map
import com.gitlab.kordlib.kordx.commands.argument.result.extension.switchOnFail

/**
 * Wraps both [this] and [other] in an Argument that succeeds if any one of them succeeds.
 *
 * ```kotlin
 * command("example") {
 *     invoke(IntArgument or StringArgument) { either ->
 *         when(either) {
 *             is Either.Left -> doStuffWith(left)
 *             is Either.Right -> doStuffWith(right)
 *         }
 *     }
 * }
 *
 */
infix fun <A, B, CONTEXT> Argument<A, CONTEXT>.or(other: Argument<B, CONTEXT>): Argument<Either<A,B>, CONTEXT> = EitherArgument(this, other)

sealed class Either<A, B> {

    operator fun component1(): A? = left
    operator fun component2(): B? = right

    abstract val left: A?
    abstract val right: B?

    class Left<A, B>(override val left: A) : Either<A, B>() {

        override val right: B?
            get() = null

    }

    class Right<A, B>(override val right: B) : Either<A, B>() {

        override val left: A?
            get() = null
    }

}

val <T, A: T, B:T> Either<A,B>.value get() = when(this) {
    is Either.Left -> left
    is Either.Right -> right
}

private class EitherArgument<A, B, CONTEXT>(
        private val left: Argument<A, CONTEXT>,
        private val right: Argument<B, CONTEXT>,
        override val name: String = "${left.name} or ${right.name}"
) : Argument<Either<A, B>, CONTEXT> {
    override val example: String
        get() = "$left or $right"

    @Suppress("RemoveExplicitTypeArguments")
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): ArgumentResult<Either<A, B>> {
        val left: ArgumentResult<Either<A, B>> = left.parse(words, fromIndex, context).map { Either.Left<A,B>(it) }
        return left.switchOnFail { right.parse(words, fromIndex, context).map { Either.Right<A,B>(it) } }
    }

}

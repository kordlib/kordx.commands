package dev.kordx.commands.argument.extension

import dev.kordx.commands.argument.Argument
import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.result.extension.map
import dev.kordx.commands.argument.result.extension.switchOnFail

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
infix fun <A, B, CONTEXT> dev.kordx.commands.argument.Argument<A, CONTEXT>.or(
        other: dev.kordx.commands.argument.Argument<B, CONTEXT>
): dev.kordx.commands.argument.Argument<Either<A, B>, CONTEXT> = EitherArgument(this, other)

/**
 * Converts an Either argument where both sides have the same type to an Argument of that type.
 * Either the [Either.left] or [Either.right] will be emitted on success.
 */
fun <T, CONTEXT> dev.kordx.commands.argument.Argument<Either<T, T>, CONTEXT>.flatten(): dev.kordx.commands.argument.Argument<T, CONTEXT> = object :
    dev.kordx.commands.argument.Argument<T, CONTEXT> {
    override val name: String
        get() = this@flatten.name

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        return this@flatten.parse(text, fromIndex, context).map { (it.left ?: it.right)!! }
    }
}

/**
 * Represents one of two possible values, [left] or [right].
 */
sealed class Either<A, B> {

    /**
     * Gets the left value, if present.
     */
    operator fun component1(): A? = left

    /**
     * Gets the right value, if present.
     */
    operator fun component2(): B? = right

    /**
     * The left value, if present.
     */
    abstract val left: A?

    /**
     * The right value, if present.
     */
    abstract val right: B?

    /**
     * Represents a present [left] value and a missing right value.
     *
     */
    class Left<A, B>(override val left: A) : Either<A, B>() {

        override val right: B?
            get() = null

    }

    /**
     * Represents a [right] value and a missing left value.
     */
    class Right<A, B>(override val right: B) : Either<A, B>() {

        override val left: A?
            get() = null
    }

}

/**
 * Gets the present value in either left or right.
 */
val <T, A : T, B : T> Either<A, B>.value
    get() = when (this) {
        is Either.Left -> left
        is Either.Right -> right
    }

private class EitherArgument<A, B, CONTEXT>(
    private val left: dev.kordx.commands.argument.Argument<A, CONTEXT>,
    private val right: dev.kordx.commands.argument.Argument<B, CONTEXT>,
    override val name: String = "${left.name} or ${right.name}"
) : dev.kordx.commands.argument.Argument<Either<A, B>, CONTEXT> {

    @Suppress("RemoveExplicitTypeArguments")
    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<Either<A, B>> {
        val left: ArgumentResult<Either<A, B>> = left.parse(text, fromIndex, context).map { Either.Left<A, B>(it) }
        return left.switchOnFail { right.parse(text, fromIndex, context).map { Either.Right<A, B>(it) } }
    }

}

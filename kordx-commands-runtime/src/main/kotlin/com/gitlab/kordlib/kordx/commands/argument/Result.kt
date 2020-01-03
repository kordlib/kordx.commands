package com.gitlab.kordlib.kordx.commands.argument

import com.gitlab.kordlib.kordx.commands.argument.Result.Failure
import com.gitlab.kordlib.kordx.commands.argument.Result.Success

/**
 * The product of an [Argument]'s parsing. A [Result] can either be a [Success] or a [Failure]
 */
sealed class Result<T> {
    /**
     * A successful parsing, producing an [item] and the amount of [wordsTaken] to parse said item.
     */
    data class Success<T>(val item: T, val wordsTaken: Int) : Result<T>() {
        companion object
    }

    /**
     * A failed parsing, the [Argument] couldn't turn the given words into an item [T], producing a [reason] for failure
     * and a specific [atWord] indication at which of the given words the [Argument] failed parsing.
     */
    data class Failure<T>(val reason: String, val atWord: Int) : Result<T>() {
        companion object
    }

    companion object {
        /**
         * Turns the given data in a [Result.Success] if [item] is not null, or defers to [failure] otherwise.
         */
        fun <T : Any> notNull(item: T?, wordsTaken: Int, failure: () -> Result.Failure<T>): Result<T> = when (item) {
            null -> failure()
            else -> Success(item, wordsTaken)
        }
    }
}

/**
 * The result of a filter action, a [FilterResult] can either [Pass] or [Fail].
 */
sealed class FilterResult {

    /**
     * A successful filter attempt, the value that matched with this result will be propagated.
     */
    object Pass : FilterResult()

    /**
     * A failed filter attempt, supplying a [reason] for failure.
     */
    class Fail(val reason: String) : FilterResult() {
        companion object
    }

    companion object {
        inline operator fun invoke(predicate: Boolean, failureReason: () -> String) = when {
            predicate -> Pass
            else -> Fail(failureReason())
        }
    }
}

/**
 * The result of a mapping action, a [MapResult] can either [Pass] or [Fail].
 */
sealed class MapResult<T> {

    /**
     * A successful mapping attempt, the [item] will be propagated.
     */
    class Pass<T>(val item: T) : MapResult<T>() {
        companion object
    }

    /**
     * A failed mapping attempt, supplying a [reason] for failure.
     */
    class Fail<T>(val reason: String) : MapResult<T>() {
        companion object
    }

    companion object {

        inline fun <reified R> filterInstance(item: Any, failMessage: String): MapResult<R> = when (item) {
            is R -> Pass(item)
            else -> Fail(failMessage)
        }

        inline fun <reified R> filterInstance(item: Any, failMessage: () -> String): MapResult<R> = when (item) {
            is R -> Pass(item)
            else -> Fail(failMessage())
        }

    }
}

/**
 * Applies the [mapper] to the [Result.Success.item] if this is a [Result.Success] returning a [Result.Success] with
 * the new value, or a [Result.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Success -> Success(mapper(item), wordsTaken)
    is Failure -> this as Result<R>
}

/**
 * Returns the result of the [generator] if this is a [Result.Failure], or [this] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.switchOnFail(generator: () -> Result<T>): Result<T> = when (this) {
    is Success -> this
    is Failure -> generator()
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the result of [mapper] is a [MapResult.Pass],
 * or a [Result.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T, R : Any> Result<T>.tryMap(mapper: (T) -> MapResult<R>): Result<R> = when (this) {
    is Success -> when (val result = mapper(item)) {
        is MapResult.Pass -> Success(result.item, wordsTaken)
        is MapResult.Fail -> Failure(result.reason, 0)
    }
    is Failure -> this as Result<R>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the result of [filter] is a [FilterResult.Pass],
 * or a [Result.Failure] otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.filter(filter: (T) -> FilterResult): Result<T> = when (this) {
    is Success -> when (val result = filter(item)) {
        FilterResult.Pass -> this
        is FilterResult.Fail -> Failure(result.reason, 0)
    }
    is Failure -> this
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is [T],
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is not [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Result<*>.filterIsInstance(failMessage: String): Result<T> = when (val result = this) {
    is Success -> when (result.item) {
        is T -> result as Result<T>
        else -> Failure(failMessage, 0)
    }
    is Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is [T],
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is not [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Result<*>.filterIsInstance(failMessage: () -> String): Result<T> = when (val result = this) {
    is Success -> when (result.item) {
        is T -> result as Result<T>
        else -> Failure(failMessage(), 0)
    }
    is Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is not null,
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is null.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T?>.filterNotNull(failMessage: String): Result<T> = when (this) {
    is Success -> when (item) {
        null -> Failure(failMessage, wordsTaken)
        else -> Success(item, wordsTaken)
    }
    is Failure -> this as Result<T>
}

/**
 * Returns a [Result.Success] if this result is a [Result.Success] *and* the [Result.Success.item] is not null,
 * or a [Result.Failure] otherwise.
 *
 * @param [failMessage] the message to supply when the [Result.Success.item] is null.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Any> Result<T?>.filterNotNull(failMessage: (T?) -> String): Result<T> = when (this) {
    is Success -> when (item) {
        null -> Failure(failMessage(item), wordsTaken)
        else -> Success(item, wordsTaken)
    }
    is Failure -> this as Result<T>
}

/**
 * Returns this if this is a [Result.Success] and [Result.Success.item] is not null, or a [Result.Success] with
 * the [default] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T?>.orDefault(default: T): Result.Success<T> = when (this) {
    is Success -> when (item) {
        null -> Success(default, wordsTaken)
        else -> this as Result.Success<T>
    }
    is Failure -> Success(default, 0)
}

/**
 * Returns this if this is a [Result.Success] and [Result.Success.item] is not null, or a [Result.Success] with
 * the [fallback] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Any> Result<T?>.orSupplyDefault(fallback: () -> T): Result.Success<T> = when (this) {
    is Success -> when (item) {
        null -> Success(fallback(), wordsTaken)
        else -> this as Result.Success<T>
    }
    is Failure -> Success(fallback(), 0)
}


/**
 * Returns this if this is a [Result.Success], or a [Result.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T> Result<T>.orElse(fallBack: T): Result.Success<T> = when (this) {
    is Success -> Success(item, wordsTaken)
    is Failure -> Success(fallBack, 0)
}

/**
 * Returns this if this is a [Result.Success], or a [Result.Success] with the [fallBack] as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.orElse(fallBack: () -> T): Result.Success<T> = when (this) {
    is Success -> Success(item, wordsTaken)
    is Failure -> Success(fallBack(), 0)
}

/**
 * Returns this if this is a [Result.Success], or a [Result.Success] with null as item otherwise.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T>.optional(): Result.Success<T?> = when (this) {
    is Success -> this as Result.Success<T?>
    is Failure -> Success(null, 0)
}

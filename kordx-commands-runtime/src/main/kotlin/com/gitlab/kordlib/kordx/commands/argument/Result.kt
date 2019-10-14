package com.gitlab.kordlib.kordx.commands.argument

sealed class Result<T> {
    data class Success<T>(val item: T, val wordsTaken: Int) : Result<T>(){
        companion object
    }

    data class Failure<T>(val reason: String, val atWord: Int) : Result<T>() {
        companion object
    }

    companion object {
        fun<T: Any> notNull(item: T?, wordsTaken: Int, failure: () -> Result.Failure<T>): Result<T> = when(item) {
            null -> failure()
            else -> Success(item, wordsTaken)
        }
    }
}

sealed class FilterResult {
    object Pass : FilterResult()

    class Fail(val reason: String) : FilterResult() {
        companion object
    }

    companion object
}

sealed class MapResult<T> {
    class Pass<T>(val item: T) : MapResult<T>() {
        companion object
    }

    class Fail<T>(val reason: String) : MapResult<T>() {
        companion object
    }

    companion object
}

@Suppress("UNCHECKED_CAST")
inline fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(mapper(item), wordsTaken)
    is Result.Failure -> this as Result<R>
}

@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.switchOnFail(generator: () -> Result<T>): Result<T> = when (this) {
    is Result.Success -> this
    is Result.Failure -> generator()
}

@Suppress("UNCHECKED_CAST")
inline fun <T, R : Any> Result<T>.tryMap(mapper: (T) -> MapResult<R>): Result<R> = when (this) {
    is Result.Success -> when (val result = mapper(item)) {
        is MapResult.Pass -> Result.Success(result.item, wordsTaken)
        is MapResult.Fail -> Result.Failure(result.reason, 0)
    }
    is Result.Failure -> this as Result<R>
}

@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.filter(filter: (T) -> FilterResult): Result<T> = when (this) {
    is Result.Success -> when (val result = filter(item)) {
        FilterResult.Pass -> this
        is FilterResult.Fail -> Result.Failure(result.reason, 0)
    }
    is Result.Failure -> this
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T?>.filterNotNull(failMessage: String): Result<T> = when (this) {
    is Result.Success -> when (item) {
        null -> Result.Failure(failMessage, wordsTaken)
        else -> Result.Success(item, wordsTaken)
    }
    is Result.Failure -> this as Result<T>
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T?>.orDefault(fallBack: T): Result.Success<T> = when (this) {
    is Result.Success -> when(item) {
        null -> Result.Success(fallBack, wordsTaken)
        else -> this as Result.Success<T>
    }
    is Result.Failure -> Result.Success(fallBack, 0)
}

@Suppress("UNCHECKED_CAST")
fun <T> Result<T>.orElse(fallBack: T): Result.Success<T> = when (this) {
    is Result.Success -> Result.Success(item, wordsTaken)
    is Result.Failure -> Result.Success(fallBack, 0)
}

@Suppress("UNCHECKED_CAST")
inline fun <T> Result<T>.orElse(fallBack: () -> T): Result.Success<T> = when (this) {
    is Result.Success -> Result.Success(item, wordsTaken)
    is Result.Failure -> Result.Success(fallBack(), 0)
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Result<T>.optional(): Result.Success<T?> = when (this) {
    is Result.Success -> this as Result.Success<T?>
    is Result.Failure -> Result.Success(null, 0)
}

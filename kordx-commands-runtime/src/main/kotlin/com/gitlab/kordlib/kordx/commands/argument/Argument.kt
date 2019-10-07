package com.gitlab.kordlib.kordx.commands.argument

interface Argument<T> {
    val name: String
    val example: String

    suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T>
}

abstract class SingleWordArgument<T> : Argument<T> {

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T> {
        if (words.size >= fromIndex) return Result.Failure("expected $name (e.g. $example)", 0)
        return parse(words[fromIndex], context)
    }

    abstract suspend fun parse(word: String, context: ParsingContext): Result<T>

    protected fun <T> success(value: T): Result<T> = Result.Success(value, 1)
    protected fun <T> failure(reason: String): Result<T> = Result.Failure(reason, 0)
}

abstract class FixedLengthArgument<T> : Argument<T> {

    abstract val length: Int

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T> {
        if (words.size - (fromIndex + 1) < length) return Result.Failure("expected $name: ($length words) (e.g. $example)", 0)
        return parse(words.slice(fromIndex..(fromIndex + length)), context)
    }

    abstract suspend fun parse(words: List<String>, context: ParsingContext): Result<T>

    protected fun <T> success(value: T): Result<T> = Result.Success(value, length)
    protected fun <T> failure(reason: String, atWord: Int = 0): Result<T> = Result.Failure(reason, atWord)

}

abstract class VariableLengthArgument<T> : Argument<T> {

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T> {
        return parse(words.drop(fromIndex), context)
    }

    abstract suspend fun parse(words: List<String>, context: ParsingContext): Result<T>


    protected fun <T> success(value: T, wordsTaken: Int): Result<T> = Result.Success(value, wordsTaken)
    protected fun <T> failure(reason: String, atWord: Int = 0): Result<T> = Result.Failure(reason, atWord)

}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Argument<T>.optional(): Argument<T?> = object : Argument<T?> by this as Argument<T?> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T?> {
        return this@optional.parse(words, fromIndex, context).optional()
    }

}

@Suppress("UNCHECKED_CAST")
fun <T, R> Argument<T>.map(mapper: (T) -> R): Argument<R> = object : Argument<R> by this as Argument<R> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<R> {
        return this@map.parse(words, fromIndex, context).map(mapper)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T, R : Any> Argument<T>.tryMap(mapper: suspend ParsingContext.(T) -> MapResult<R>): Argument<R> = object : Argument<R> by this as Argument<R> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<R> {
        return this@tryMap.parse(words, fromIndex, context).tryMap { mapper.invoke(context, it) }
    }
}

fun <T> Argument<T>.example(example: () -> String): Argument<T> = object : Argument<T> by this {

    override val example: String
        get() = example()
}

fun <T> Argument<T>.filter(filter: suspend ParsingContext.(T) -> FilterResult): Argument<T> = object : Argument<T> by this {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T> {
        return this@filter.parse(words, fromIndex, context).filter { filter(context, it) }
    }
}

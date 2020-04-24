package com.gitlab.kordlib.kordx.commands.argument

import com.gitlab.kordlib.kordx.commands.argument.result.Result

interface Argument<T, in CONTEXT> {
    val name: String
    val example: String

    suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T>
}

abstract class SingleWordArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        if (words.size <= fromIndex) return Result.Failure("expected $name (e.g. $example)", 0)
        return parse(words[fromIndex], context)
    }

    abstract suspend fun parse(word: String, context: CONTEXT): Result<T>

    protected fun <T> success(value: T): Result<T> = Result.Success(value, 1)
    protected fun <T> failure(reason: String): Result<T> = Result.Failure(reason, 0)
}

abstract class FixedLengthArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    abstract val length: Int

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        if (words.size - (fromIndex + 1) < length) return Result.Failure("expected $name: ($length words) (e.g. $example)", 0)
        return parse(words.slice(fromIndex..(fromIndex + length)), context)
    }

    abstract suspend fun parse(words: List<String>, context: CONTEXT): Result<T>

    protected fun <T> success(value: T): Result<T> = Result.Success(value, length)
    protected fun <T> failure(reason: String, atWord: Int = 0): Result<T> = Result.Failure(reason, atWord)

}

abstract class VariableLengthArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    final override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return parse(words.drop(fromIndex), context)
    }

    abstract suspend fun parse(words: List<String>, context: CONTEXT): Result<T>


    protected fun <T> success(value: T, wordsTaken: Int): Result<T> = Result.Success(value, wordsTaken)
    protected fun <T> failure(reason: String, atWord: Int = 0): Result<T> = Result.Failure(reason, atWord)

}

package com.gitlab.kordlib.kordx.commands.argument.util

import com.gitlab.kordlib.kordx.commands.argument.*


@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(): Argument<T?, CONTEXT> =
        object : Argument<T?, CONTEXT> by this as Argument<T?, CONTEXT> {

            override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T?> {
                return this@optional.parse(words, fromIndex, context).optional()
            }

        }

@Suppress("UNCHECKED_CAST")
fun <T, R, CONTEXT> Argument<T, CONTEXT>.map(mapper: (T) -> R): Argument<R, CONTEXT> =
        object : Argument<R, CONTEXT> by this as Argument<R, CONTEXT> {

            override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<R> {
                return this@map.parse(words, fromIndex, context).map(mapper)
            }
        }

@Suppress("UNCHECKED_CAST")
fun <T, R : Any, CONTEXT> Argument<T, CONTEXT>.tryMap(
        mapper: suspend CONTEXT.(T) -> MapResult<R>
): Argument<R, CONTEXT> = object : Argument<R, CONTEXT> by this as Argument<R, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<R> {
        return this@tryMap.parse(words, fromIndex, context).tryMap { mapper.invoke(context, it) }
    }
}

fun <T, CONTEXT> Argument<T, CONTEXT>.example(example: () -> String): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override val example: String
        get() = example()
}

fun <T, CONTEXT> Argument<T, CONTEXT>.filter(
        filter: suspend CONTEXT.(T) -> FilterResult
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@filter.parse(words, fromIndex, context).filter { filter(context, it) }
    }
}

fun <T, CONTEXT> Argument<T, CONTEXT>.withDefault(default: T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orElse(default)
    }
}

fun <T : Any, CONTEXT> Argument<T, CONTEXT>.withDefault(default: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orElse<T> { default(context) }
    }
}
package com.gitlab.kordlib.kordx.commands.argument.util

import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.command.CommandContext


@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(): Argument<T?, CONTEXT> =
        object : Argument<T?, CONTEXT> by this as Argument<T?, CONTEXT> {

            override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T?> {
                return this@optional.parse(words, fromIndex, context).optional()
            }

        }


fun <T : Any, CONTEXT> Argument<T, CONTEXT>.optional(default: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@optional.parse(words, fromIndex, context).orElse(default(context))
    }
}

@Suppress("UNCHECKED_CAST")
fun <T, R, CONTEXT> Argument<T, CONTEXT>.map(mapper: suspend CONTEXT.(T) -> R): Argument<R, CONTEXT> =
        object : Argument<R, CONTEXT> by this as Argument<R, CONTEXT> {

            override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<R> {
                return this@map.parse(words, fromIndex, context).map { mapper(context, it) }
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

@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.filterNotNull(
        failMessage: String
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@filterNotNull.parse(words, fromIndex, context).filterNotNull(failMessage)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.filterNotNull(
        failMessage: suspend (T?) -> String
): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@filterNotNull.parse(words, fromIndex, context).filterNotNull { failMessage(it) }
    }
}

fun <T : Any, CONTEXT> Argument<T, CONTEXT>.withDefault(default: T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orElse(default)
    }
}

fun <T : Any, CONTEXT> Argument<T, CONTEXT>.withDefault(default: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orElse<T> { default(context) }
    }
}

@Suppress("UNCHECKED_CAST")
@JvmName("withDefaultNullable")
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.withDefault(default: T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orDefault(default)
    }
}

@Suppress("UNCHECKED_CAST")
@JvmName("withDefaultNullable")
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.withDefault(fallback: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orSupplyDefault { fallback(context) }
    }
}

fun <T, CONTEXT, NEWCONTEXT : CONTEXT> Argument<T, CONTEXT>.withContext(newContext: CommandContext<*, NEWCONTEXT, *>): Argument<T, NEWCONTEXT> = this
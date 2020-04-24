package com.gitlab.kordlib.kordx.commands.argument.extensions

import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.result.*
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.orDefault
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.orElse
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.orElseSupply
import com.gitlab.kordlib.kordx.commands.argument.result.extensions.orSupplyDefault

fun <T : Any, CONTEXT> Argument<T, CONTEXT>.withDefault(default: T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orElse(default)
    }
}

fun <T : Any, CONTEXT> Argument<T, CONTEXT>.withDefault(default: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefault.parse(words, fromIndex, context).orElseSupply { default(context) }
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
fun <T : Any, CONTEXT> Argument<T?, CONTEXT>.withDefaultSupply(fallback: suspend CONTEXT.() -> T): Argument<T, CONTEXT> = object : Argument<T, CONTEXT> by this as Argument<T, CONTEXT> {
    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<T> {
        return this@withDefaultSupply.parse(words, fromIndex, context).orSupplyDefault { fallback(context) }
    }
}
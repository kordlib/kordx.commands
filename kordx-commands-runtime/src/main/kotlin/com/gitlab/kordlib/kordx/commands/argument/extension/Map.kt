package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.result.Result
import com.gitlab.kordlib.kordx.commands.argument.result.extension.MapResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.map
import com.gitlab.kordlib.kordx.commands.argument.result.extension.tryMap

/**
 * Returns an Argument that maps all successful values of the supplied Argument to [R] through the [mapper].
 * Emits failure if the supplied Argument fails.
 */
@Suppress("UNCHECKED_CAST")
fun <T, R, CONTEXT> Argument<T, CONTEXT>.map(mapper: suspend CONTEXT.(T) -> R): Argument<R, CONTEXT> =
        object : Argument<R, CONTEXT> by this as Argument<R, CONTEXT> {

            override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<R> {
                return this@map.parse(words, fromIndex, context).map { mapper(context, it) }
            }
        }

/**
 * Returns an Argument that maps all successful values of the supplied Argument to [R] through the [mapper].
 * Emits failure if the supplied Argument fails or the [mapper] emits a failure.
 */
@Suppress("UNCHECKED_CAST")
fun <T, R : Any, CONTEXT> Argument<T, CONTEXT>.tryMap(
        mapper: suspend CONTEXT.(T) -> MapResult<R>
): Argument<R, CONTEXT> = object : Argument<R, CONTEXT> by this as Argument<R, CONTEXT> {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<R> {
        return this@tryMap.parse(words, fromIndex, context).tryMap { mapper.invoke(context, it) }
    }
}

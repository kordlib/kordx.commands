package dev.kordx.commands.argument.extension

import dev.kordx.commands.argument.*
import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.result.extension.MapResult
import dev.kordx.commands.argument.result.extension.map
import dev.kordx.commands.argument.result.extension.tryMap

/**
 * Returns an Argument that maps all successful values of the supplied Argument to [R] through the [mapper].
 * Emits failure if the supplied Argument fails.
 */
@Suppress("UNCHECKED_CAST")
fun <T, R, CONTEXT> dev.kordx.commands.argument.Argument<T, CONTEXT>.map(mapper: suspend CONTEXT.(T) -> R): dev.kordx.commands.argument.Argument<R, CONTEXT> =
        object : dev.kordx.commands.argument.Argument<R, CONTEXT> by this as dev.kordx.commands.argument.Argument<R, CONTEXT> {

            override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<R> {
                return this@map.parse(text, fromIndex, context).map { mapper(context, it) }
            }
        }

/**
 * Returns an Argument that maps all successful values of the supplied Argument to [R] through the [mapper].
 * Emits failure if the supplied Argument fails or the [mapper] emits a failure.
 */
@Suppress("UNCHECKED_CAST")
fun <T, R : Any, CONTEXT> dev.kordx.commands.argument.Argument<T, CONTEXT>.tryMap(
        mapper: suspend CONTEXT.(T) -> MapResult<R>
): dev.kordx.commands.argument.Argument<R, CONTEXT> = object : dev.kordx.commands.argument.Argument<R, CONTEXT> by this as dev.kordx.commands.argument.Argument<R, CONTEXT> {

    override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<R> {
        return this@tryMap.parse(text, fromIndex, context).tryMap(fromIndex) { mapper.invoke(context, it) }
    }
}

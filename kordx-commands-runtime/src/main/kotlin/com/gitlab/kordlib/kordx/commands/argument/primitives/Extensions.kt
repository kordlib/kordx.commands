package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.*
import kotlin.random.Random

fun <T> Argument<T>.inRange(range: ClosedRange<T>): Argument<T> where T : Number, T : Comparable<T> = object : Argument<T> by this {

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override val example: String
        get() = when (range.start) {
            is Float, is Double -> Random.nextDouble(range.start.toDouble(), range.endInclusive.toDouble())
            else -> Random.nextLong(range.start.toLong(), range.endInclusive.toLong())
        }.toString()

    override suspend fun parse(words: List<String>, fromIndex: Int, context: ParsingContext): Result<T> {
        return this@inRange.parse(words, fromIndex, context).filter {
            if (it in range) FilterResult.Pass
            else FilterResult.Fail("expected number in range of $range")
        }
    }

}

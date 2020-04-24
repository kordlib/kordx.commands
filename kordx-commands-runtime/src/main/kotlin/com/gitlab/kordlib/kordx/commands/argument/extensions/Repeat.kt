package com.gitlab.kordlib.kordx.commands.argument.extensions

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.Result

fun<T, CONTEXT> Argument<T, CONTEXT>.repeated(
        range: IntRange = 0..Int.MAX_VALUE,
        name: String = "${this.name} repeated"
): Argument<List<T>, CONTEXT> = RepeatArg(this, range.first, range.last, name)

private class RepeatArg<T, CONTEXT>(
        val argument: Argument<T, CONTEXT>,
        private val minRepeats: Int = 0,
        private val maxRepeats: Int = Int.MAX_VALUE,
        override val name: String = "${argument.name} repeated"
) : Argument<List<T>, CONTEXT> {

    init {
        require(minRepeats >= 0) { "minimum repeats needs to be at least 0 but was $minRepeats" }
        require(maxRepeats > 1) { "need to repeat at least 1 times but was $maxRepeats" }
        require(minRepeats < maxRepeats) { "minRepeats ($minRepeats) needs to be less than maxRepeats ($maxRepeats)" }
    }

    override val example: String
        get() = "${argument.example} repeated"

    override suspend fun parse(words: List<String>, fromIndex: Int, context: CONTEXT): Result<List<T>> {
        var index = fromIndex
        val results = mutableListOf<T>()
        var repeats = 0
        loop@while (index < words.size && repeats < maxRepeats) {
            when(val result = argument.parse(words, index, context)) {
                is Result.Failure -> break@loop
                is Result.Success -> {
                    results += result.item
                    index += result.wordsTaken
                    repeats += 1
                }
            }
        }

        return when {
            results.size < minRepeats -> Result.Failure("expected at least $minRepeats repeats of ${argument.name}, but only got ${results.size}", index)
            else -> Result.Success(results, index - fromIndex)
        }
    }

}
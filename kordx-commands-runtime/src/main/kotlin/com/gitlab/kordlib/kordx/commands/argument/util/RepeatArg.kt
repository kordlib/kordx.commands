package com.gitlab.kordlib.kordx.commands.argument.util

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.FixedLengthArgument
import com.gitlab.kordlib.kordx.commands.argument.Result


sealed class RepeatAmount {
    abstract val times: Int

    object Infinite : RepeatAmount() {
        override val times = Int.MAX_VALUE
    }
    class Fixed(override val times: Int) : RepeatAmount()
}

class RepeatArg<T, CONTEXT>(
        val argument: FixedLengthArgument<T, CONTEXT>,
        private val minRepeats: Int = 0,
        private val maxRepeats: Int = Int.MAX_VALUE,
        override val name: String = "${argument.name} repeated"
) : Argument<List<T>, CONTEXT> {

    init {
        require(minRepeats > 0) { "minimum repeats needs to be at least 0 but was $minRepeats" }
        require(maxRepeats > 1) { "need to repeat at least 1 times but was $maxRepeats" }
        require(minRepeats < maxRepeats) { "minRepeats ($minRepeats) needs to be less than maxRepeats ($maxRepeats)" }
    }

    override val example: String
        get() = argument.example

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
            results.size < minRepeats -> Result.Failure("expected at least $minRepeats repeats of ${argument.name}, but got ${results.size}", index)
            else -> Result.Success(results, index - fromIndex)
        }
    }

}
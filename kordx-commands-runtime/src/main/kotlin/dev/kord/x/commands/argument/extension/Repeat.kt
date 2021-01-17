package dev.kord.x.commands.argument.extension

import dev.kord.x.commands.argument.Argument
import dev.kord.x.commands.argument.result.ArgumentResult

/**
 * Returns an argument that accepts this argument multiple times.
 *
 * @param range the range of acceptable repeats. If the amount of repeats is less than the minimum of [range], the
 * argument will fail. Once the maximum amount of repeats has been reached the argument will return a success with
 * that amount.
 *
 * @param name The name of this argument.
 *
 * @throws IllegalArgumentException if the range starts at a value that is not > 0 or has a minimum value higher
 * than the maximum value.
 */
fun <T, CONTEXT> Argument<T, CONTEXT>.repeated(
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

    override suspend fun parse(
        text: String,
        fromIndex: Int,
        context: CONTEXT
    ): ArgumentResult<List<T>> {
        var index = fromIndex
        val results = mutableListOf<T>()
        var repeats = 0
        loop@ while (index < text.length && repeats < maxRepeats) {
            when (val result = argument.parse(text, index, context)) {
                is ArgumentResult.Failure -> break@loop
                is ArgumentResult.Success -> {
                    results += result.item
                    index = result.newIndex
                    repeats += 1
                }
            }
        }

        return when {
            results.size < minRepeats -> ArgumentResult.Failure(
                "expected at least $minRepeats repeats of ${argument.name}, but only got ${results.size}",
                index
            )
            else -> ArgumentResult.Success(results, index - fromIndex)
        }
    }

}

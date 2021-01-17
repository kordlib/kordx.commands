package dev.kord.x.commands.argument.result

/**
 * A Parsing result for [Arguments][dev.kord.x.commands.argument.Argument] that
 * only parses whole words.
 *
 * Conceptually, a word is a sequence of [Char], collected until the first whitespace character
 * is encountered.
 */
sealed class WordResult<T> {

    /**
     * The word at which the result succeeded or failed,
     */
    abstract val wordsTaken: Int

    /**
     * A successful parsing of words. an [item] is produced after parsing [wordsTaken] word.
     * ```
     * ["5", "seconds"] -> Success(5, 1)
     * ["hello", "world"] -> Success(["Hello", "world"], 2)
     * ```
     */
    data class Success<T>(val item: T, override val wordsTaken: Int) : WordResult<T>()

    /**
     * A failed parsing of words. a [message] is produced after parsing the words up until the index of [wordsTaken].
     * ```
     * ["cat", "seconds"] -> Failure("expected a number", 0)
     * ["hello", "planet"] -> Failure("expected world", 1)
     * ```
     */
    data class Failure<T>(val message: String, override val wordsTaken: Int) : WordResult<T>()

    /**
     * Converts the result to an [ArgumentResult], adding the length of the [word] to the [currentIndex].
     */
    fun toArgumentResult(currentIndex: Int, word: String): ArgumentResult<T> = when (this) {
        is Success -> ArgumentResult.Success(item, currentIndex + if(wordsTaken > 0) word.length else 0)
        is Failure -> ArgumentResult.Failure(message, currentIndex + if(wordsTaken > 0) word.length else 0)
    }

    /**
     * Converts the result to an [ArgumentResult] with the [newIndex].
     */
    fun toArgumentResult(newIndex: Int): ArgumentResult<T> = when (this) {
        is Success -> ArgumentResult.Success(item, newIndex)
        is Failure -> ArgumentResult.Failure(message, newIndex)
    }
}

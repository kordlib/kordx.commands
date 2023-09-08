package dev.kordx.commands.argument

import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.result.WordResult
import dev.kordx.commands.argument.state.*

/**
 * A parser that takes in a set of words and a [CONTEXT], producing a [ArgumentResult] with a possible generated [T].
 *
 * Arguments come with a set of extension functions to further customize them to your requirements:
 * * [optional] and [withDefault] will replace parsing failures with default values.
 * * [map] and [tryMap] allow mapping to more complex types from more basic arguments.
 * * [filter] and [filterIsInstance] will reduce the scope of allowed values.
 * * [or] can be used to accept one of two arguments in the same position.
 * * [repeated] can be used to accept a range of the same argument.
 *
 */
interface Argument<out T, in CONTEXT> {
    /**
     * The name of the argument, used for user-side
     */
    val name: String

    /**
     * Parses the given [text], reading the remaining chars starting from [fromIndex].
     * Returns a [ArgumentResult] which represents either a failure or success in parsing a certain amount of words.
     */
    suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T>


    companion object {

        internal val whitespaceRegex = Regex("\\s")

    }
}

/**
 * Utility class for Arguments that expect one word.
 */
abstract class SingleWordArgument<T, CONTEXT> : StateArgument<T, CONTEXT>() {

    override suspend fun ParseState.parse(context: CONTEXT): ArgumentResult<T> {
        if(ended) return unexpectedEnd()

        val cursorBefore = cursor
        val word = flush { consumeWord() }

        if(word.isBlank()){
            return ArgumentResult.Failure("Expected word.", cursor)
        }

        val result = parse(word, context)
        require(result.wordsTaken <= 1) {
            "Single word arguments cannot take more than one word: $result"
        }

        return result.toArgumentResult(cursorBefore, word)
    }

    /**
     * Convenience method that parses the first word only.
     */
    abstract suspend fun parse(word: String, context: CONTEXT): WordResult<T>


    /**
     * Convenience method that creates a success for one word taken.
     */
    protected fun <T> success(value: T): WordResult<T> = WordResult.Success(value, 1)

    /**
     * Convenience method that creates a failure for no words taken.
     */
    protected fun <T> failure(reason: String): WordResult<T> = WordResult.Failure(reason, 0)
}

/**
 * Utility class for Arguments that expect a fixed amount of words.
 */
abstract class FixedLengthArgument<T, CONTEXT> : StateArgument<T, CONTEXT>() {

    /**
     * The expected word length. Word lists with fewer words will automatically return a [failure].
     */
    abstract val length: Int

    override suspend fun ParseState.parse(context: CONTEXT): ArgumentResult<T> {
        @OptIn(ExperimentalStdlibApi::class)
        val words = buildList {
            repeat(length) {
                dropWhiteSpace()
                if (ended) {
                    return ArgumentResult.Failure("expected at least $length words", 0)
                }

                add(flush { consumeWord() })

            }
        }

        val result = parse(words, context)

        /**
         * Now that we know how many words to take, we'll translate this back to characters.
         * One might assume that this would simply be: (length of n words) + (n - 1).
         * You'd be wrong, some targets (like Discord) accept multiple spaces between words.
         * This seems like a sane solution given the information.
         */
        reset()
        repeat(result.wordsTaken){
            dropWhiteSpace()
            //TODO: contemplate the need for a `dropword`, or rethink the whole convention.
            dropWhile { !it.isWhitespace() } //don't consume word, saves on allocations
        }

        return parse(words, context).toArgumentResult(cursor)
    }

    /**
     * Convenience method that parses a list of words with a fixed [length].
     */
    abstract suspend fun parse(words: List<String>, context: CONTEXT): WordResult<T>

    /**
     * Convenience method to create a success with a fixed [length].
     */
    protected fun <T> success(value: T): WordResult<T> = WordResult.Success(value, 1)

    /**
     * Convenience method to create a failure, defaults to the first word.
     */
    protected fun <T> failure(
            reason: String,
            atWord: Int = 0
    ): WordResult<T> = WordResult.Failure(reason, atWord)

}

/**
 * Utility class for Arguments that expect any range of words.
 */
abstract class VariableLengthArgument<T, CONTEXT> : Argument<T, CONTEXT> {

    final override suspend fun parse(text: String, fromIndex: Int, context: CONTEXT): ArgumentResult<T> {
        val words = text.substring(fromIndex).split(Argument.whitespaceRegex)
        return parse(words, context)
    }

    /**
     * Convenience method that parses the remaining amount of words.
     */
    abstract suspend fun parse(words: List<String>, context: CONTEXT): ArgumentResult<T>

    /**
     * Convenience method to create a success.
     */
    protected fun <T> success(value: T, wordsTaken: Int): ArgumentResult<T> = ArgumentResult.Success(value, wordsTaken)

    /**
     * Convenience method to create a failure, defaults to the first word.
     */
    protected fun <T> failure(
            reason: String,
            atWord: Int = 0
    ): ArgumentResult<T> = ArgumentResult.Failure(reason, atWord)

}

package com.gitlab.kordlib.kordx.commands.model.prefix

/**
 * A parser of events [T] that either [accepts][PrefixRule.Result.Accepted] a message, supplying it with a prefix, or
 * [denies][PrefixRule.Result.Denied] it, indicating that the message should be rejected.
 */
interface PrefixRule<in T> {

    /**
     * Consumes the [context] and its [message], [accepting][PrefixRule.Result.Accepted] or
     * [denying][PrefixRule.Result.Denied] it. Accepted contexts will be further processed towards invoking a command.
     */
    suspend fun consume(message: String, context: T) : Result

    /**
     * The result of parsing a message by a [PrefixRule].
     */
    sealed class Result {

        /**
         * The message was denied further processing.
         */
        object Denied : Result()

        /**
         * The message was accepted for further processing, and should be stripped of the [prefix] before doing so.
         */
        class Accepted(val prefix: String) : Result()

        /**
         * Switches the current result for the [supplier] if it is [Denied].
         */
        inline fun switchOnFail(supplier: () -> Result) : Result = when(this) {
            is Accepted -> this
            Denied -> supplier()
        }
    }

    companion object {

        /**
         * Creates a [PrefixRule] for a context [T].
         */
        inline operator fun<T> invoke(
                crossinline supplier: suspend (message: String, context: T) -> Result
        ) : PrefixRule<T> = object : PrefixRule<T> {
            override suspend fun consume(message: String, context: T): Result = supplier(message, context)
        }

        /**
         * Creates a [PrefixRule] that matches any message, return an empty string as prefix.
         */
        fun<T> none() : PrefixRule<T> = object : PrefixRule<T> {
            override suspend fun consume(message: String, context: T): Result = Result.Accepted("")
        }

    }
}

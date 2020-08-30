package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class QuotedArgumentTest {

    @Test
    fun `argument successfully parses quote`(): Unit = runBlocking {
        val argument = QuotedArgument(prefix = "'", suffix = ")")

        val text = "example of a quote with different prefix and suffix"
        val result = argument.parse(" '$text)", 0, Unit)

        assert(result is ArgumentResult.Success) { "result expected to be success but was $result" }
        result as ArgumentResult.Success

        assertEquals(text, result.item)
    }

}
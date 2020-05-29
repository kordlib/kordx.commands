package com.gitlab.kordlib.kordx.commands.model.prefix

import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class LiteralPrefixRuleTest {

    @Test
    fun `literal matches against its own prefix`() = runBlockingTest {
        val literal = LiteralPrefixRule("hello")

        val result = literal.consume("hello world", null)

        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("hello", accepted.prefix)
    }

    @Test
    fun `literal does not match against another prefix`() = runBlockingTest {
        val literal = LiteralPrefixRule("hello")

        val result = literal.consume("world hello", null)

        assertEquals(PrefixRule.Result.Denied, result)
    }

}

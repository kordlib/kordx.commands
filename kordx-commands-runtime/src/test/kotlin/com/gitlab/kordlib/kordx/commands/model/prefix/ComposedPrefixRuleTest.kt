package com.gitlab.kordlib.kordx.commands.model.prefix

import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class ComposedPrefixRuleTest {

    private val composed = LiteralPrefixRule("!") or LiteralPrefixRule("+")

    @Test
    fun `composed accepts when first accepts`() = runBlockingTest {
        val result = composed.consume("!hello world", null)

        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("!", accepted.prefix)
    }

    @Test
    fun `composed accepts when second accepts`()= runBlockingTest {
        val result = composed.consume("+hello world", null)

        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("+", accepted.prefix)
    }

    @Test
    fun `composed denies when none accept`()= runBlockingTest {
        val result = composed.consume("hello world", null)

        assertEquals(PrefixRule.Result.Denied, result)
    }

}
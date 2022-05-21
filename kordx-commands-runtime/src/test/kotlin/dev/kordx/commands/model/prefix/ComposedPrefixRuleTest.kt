package dev.kordx.commands.model.prefix

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

@ExperimentalCoroutinesApi
internal class ComposedPrefixRuleTest {

    private val composed = LiteralPrefixRule("!") or LiteralPrefixRule("+")

    @Test
    fun `composed accepts when first accepts`() = runTest {
        val result = composed.consume("!hello world", null)

        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("!", accepted.prefix)
    }

    @Test
    fun `composed accepts when second accepts`()= runTest {
        val result = composed.consume("+hello world", null)

        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("+", accepted.prefix)
    }

    @Test
    fun `composed denies when none accept`()= runTest {
        val result = composed.consume("hello world", null)

        assertEquals(PrefixRule.Result.Denied, result)
    }

}
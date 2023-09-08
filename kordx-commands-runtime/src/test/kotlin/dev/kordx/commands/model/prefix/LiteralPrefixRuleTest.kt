package dev.kordx.commands.model.prefix

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

@ExperimentalCoroutinesApi
internal class LiteralPrefixRuleTest {

    @Test
    fun `literal matches against its own prefix`() = runTest {
        val literal = LiteralPrefixRule("hello")

        val result = literal.consume("hello world", null)

        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("hello", accepted.prefix)
    }

    @Test
    fun `literal does not match against another prefix`() = runTest {
        val literal = LiteralPrefixRule("hello")

        val result = literal.consume("world hello", null)

        assertEquals(PrefixRule.Result.Denied, result)
    }

}

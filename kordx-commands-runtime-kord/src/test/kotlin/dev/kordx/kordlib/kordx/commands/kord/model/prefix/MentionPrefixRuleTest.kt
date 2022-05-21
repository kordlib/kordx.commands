package dev.kordx.kordlib.kordx.commands.kord.model.prefix

import dev.kordx.commands.kord.model.prefix.mention
import dev.kordx.commands.model.prefix.PrefixRule
import kotlinx.coroutines.test.runTest
import org.gradle.internal.impldep.org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class MentionPrefixRuleTest {

    @Test
    fun `mention prefix accepts a bot's username mention`() = runTest {
        val rule = prefix { mention() }

        val event = mockEvent()

        val result = rule.consume("<@${mentionId.value}> test", event)
        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("<@${mentionId.value}> ", accepted.prefix)
    }

    @Test
    fun `mention prefix accepts a bot's nickname mention`() = runTest {
        val rule = prefix { mention() }

        val event = mockEvent()

        val result = rule.consume("<@!${mentionId.value}> test", event)
        val accepted = result as PrefixRule.Result.Accepted

        assertEquals("<@!${mentionId.value}> ", accepted.prefix)
    }

    @Test
    fun `mention prefix does not accept another mention`() = runTest {
        val rule = prefix { mention() }

        val event = mockEvent()

        val result = rule.consume("<@!${123456}> test", event)

        assertEquals(PrefixRule.Result.Denied, result)
    }

}

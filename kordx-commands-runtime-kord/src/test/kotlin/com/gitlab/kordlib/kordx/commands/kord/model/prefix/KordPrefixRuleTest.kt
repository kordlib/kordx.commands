@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(KordUnsafe::class, KordExperimental::class)

package com.gitlab.kordlib.kordx.commands.kord.model.prefix

import com.gitlab.kordlib.common.annotation.KordExperimental
import com.gitlab.kordlib.common.annotation.KordUnsafe
import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.behavior.channel.MessageChannelBehavior
import com.gitlab.kordlib.core.entity.Guild
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val mentionId = Snowflake(1337L)

inline fun mockEventAdapter(apply: KordEventAdapter.() -> Unit = {}) = mockk<KordEventAdapter> {
    apply()
}

inline fun KordEventAdapter.mockGuild(apply: Guild.() -> Unit = {}) {
    val mockGuild = mockk<Guild> { apply() }
    every { guild } returns mockGuild
}

inline fun KordEventAdapter.mockChannel(apply: MessageChannelBehavior.() -> Unit = {}) {
    val mockChannel = mockk<MessageChannelBehavior> { apply() }
    every { channel } returns mockChannel
}

inline fun KordEventAdapter.mockAuthor(apply: User.() -> Unit = {}) {
    val mockUser = mockk<User> { apply() }
    every { author } returns mockUser
}

inline fun KordEventAdapter.mockMember(apply: Member.() -> Unit = {}) {
    val mockMember = mockk<Member> { apply() }
    coEvery { resolveAuthorAsMember() } returns mockMember
}

fun KordEventAdapter.mockNoGuild() {
    every { guild } returns null
}

fun mockKord() = mockk<Kord> {
    every { selfId } returns mentionId
}

inline fun <T> prefix(apply: PrefixBuilder.() -> T) = PrefixBuilder(koinApplication {
    modules(module { single { mockKord() } })
}.koin).apply()


internal class KordPrefixRuleTest {

    @Test
    fun `guild prefix accepts a correct prefix in a guild`() = runBlockingTest {
        val guildRule = prefix { guild { "!" } }

        val eventAdapter = mockEventAdapter {
            mockGuild()
        }

        val result = guildRule.consume("!test", eventAdapter)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `guild prefix denies a correct prefix without a guild`() = runBlockingTest {
        val guildRule = prefix { guild { "!" } }

        val eventAdapter = mockEventAdapter {
            mockNoGuild()
        }

        val result = guildRule.consume("!test", eventAdapter)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `guild prefix denies a wrong prefix in a guild`() = runBlockingTest {
        val guildRule = prefix { guild { "+" } }

        val eventAdapter = mockEventAdapter {
            mockGuild()
        }

        val result = guildRule.consume("!test", eventAdapter)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `channel prefix accepts a correct prefix`() = runBlockingTest {
        val rule = prefix { channel { "!" } }

        val eventAdapter = mockEventAdapter {
            mockChannel()
        }

        val result = rule.consume("!test", eventAdapter)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `channel prefix denies a wrong prefix`() = runBlockingTest {
        val rule = prefix { channel { "+" } }

        val eventAdapter = mockEventAdapter {
            mockChannel()
        }

        val result = rule.consume("!test", eventAdapter)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `user prefix accepts a correct prefix`() = runBlockingTest {
        val rule = prefix { user { "!" } }

        val eventAdapter = mockEventAdapter {
            mockAuthor()
        }

        val result = rule.consume("!test", eventAdapter)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `user prefix denies a wrong prefix`() = runBlockingTest {
        val rule = prefix { user { "+" } }

        val eventAdapter = mockEventAdapter {
            mockAuthor()
        }

        val result = rule.consume("!test", eventAdapter)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `member prefix accepts a correct prefix`() = runBlockingTest {
        val rule = prefix { member { "!" } }

        val eventAdapter = mockEventAdapter {
            mockGuild()
            mockMember()
        }

        val result = rule.consume("!test", eventAdapter)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `member prefix denies a wrong prefix`() = runBlockingTest {
        val rule = prefix { member { "+" } }

        val eventAdapter = mockEventAdapter {
            mockGuild()
            mockMember()
        }

        val result = rule.consume("!test", eventAdapter)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

}

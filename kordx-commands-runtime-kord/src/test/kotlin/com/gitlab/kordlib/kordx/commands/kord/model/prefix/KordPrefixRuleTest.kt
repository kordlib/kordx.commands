package com.gitlab.kordlib.kordx.commands.kord.model.prefix

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.behavior.GuildBehavior
import com.gitlab.kordlib.core.behavior.channel.MessageChannelBehavior
import com.gitlab.kordlib.core.entity.Guild
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixBuilder
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixRule
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val mentionId = Snowflake(1337L)

inline fun mockEvent(apply: MessageCreateEvent.() -> Unit = {}) = mockk<MessageCreateEvent> {
    apply()
}

inline fun MessageCreateEvent.mockMessage(apply: Message.() -> Unit = {}) {
    val mockMessage = mockk<Message> { apply() }
    every { message } returns mockMessage
    every { guildId } returns null
}

inline fun Message.mockGuild(apply: GuildBehavior.() -> Unit = {}) {
    val mockGuild = mockk<Guild> { apply() }
    coEvery { getGuildOrNull() } returns mockGuild
}

inline fun Message.mockChannel(apply: MessageChannelBehavior.() -> Unit = {}) {
    val mockChannel = mockk<MessageChannelBehavior> { apply() }
    every { channel } returns mockChannel
}

inline fun Message.mockAuthor(apply: User.() -> Unit = {}) {
    val mockUser = mockk<User> { apply() }
    every { author } returns mockUser
}

inline fun Message.mockMember(apply: Member.() -> Unit = {}) {
    val mockMember = mockk<Member> { apply() }
    coEvery { getAuthorAsMember() } returns mockMember
}

fun Message.mockNoGuild() {
    coEvery { getGuildOrNull() } returns null
}

@Suppress("UNCHECKED_CAST")
fun mockKord() = mockk<Kord> {
    every { selfId } answers object: Answer<Any> {
        override fun answer(call: Call): Any = mentionId.value
    } as Answer<Snowflake>
}

inline fun <T> prefix(apply: PrefixBuilder.() -> T) = PrefixBuilder(koinApplication {
    modules(module { single { mockKord() } })
}.koin).apply()


internal class KordPrefixRuleTest {

    @Test
    fun `guild prefix accepts a correct prefix in a guild`() = runBlockingTest {
        val guildRule = prefix { guild { "!" } }

        val event = mockEvent {
            mockMessage { mockGuild() }
        }

        val result = guildRule.consume("!test", event)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `guild prefix denies a correct prefix without a guild`() = runBlockingTest {
        val guildRule = prefix { guild { "!" } }

        val event = mockEvent {
            mockMessage { mockNoGuild() }
        }

        val result = guildRule.consume("!test", event)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `guild prefix denies a wrong prefix in a guild`() = runBlockingTest {
        val guildRule = prefix { guild { "+" } }

        val event = mockEvent {
            mockMessage {
                mockGuild()
                every { guildId } returns null
            }
        }

        val result = guildRule.consume("!test", event)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `channel prefix accepts a correct prefix`() = runBlockingTest {
        val rule = prefix { channel { "!" } }

        val event = mockEvent {
            mockMessage { mockChannel() }
        }

        val result = rule.consume("!test", event)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `channel prefix denies a wrong prefix`() = runBlockingTest {
        val rule = prefix { channel { "+" } }

        val event = mockEvent {
            mockMessage { mockChannel() }
        }

        val result = rule.consume("!test", event)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `user prefix accepts a correct prefix`() = runBlockingTest {
        val rule = prefix { user { "!" } }

        val event = mockEvent {
            mockMessage { mockAuthor() }
        }

        val result = rule.consume("!test", event)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `user prefix denies a wrong prefix`() = runBlockingTest {
        val rule = prefix { user { "+" } }

        val event = mockEvent {
            mockMessage { mockAuthor() }
        }

        val result = rule.consume("!test", event)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

    @Test
    fun `member prefix accepts a correct prefix`() = runBlockingTest {
        val rule = prefix { member { "!" } }

        val event = mockEvent {
            mockMessage { mockMember() }
        }

        val result = rule.consume("!test", event)
        val accepted = result as PrefixRule.Result.Accepted

        Assertions.assertEquals("!", accepted.prefix)
    }

    @Test
    fun `member prefix denies a wrong prefix`() = runBlockingTest {
        val rule = prefix { member { "+" } }

        val event = mockEvent {
            mockMessage { mockMember() }
        }

        val result = rule.consume("!test", event)
        Assertions.assertEquals(PrefixRule.Result.Denied, result)
    }

}

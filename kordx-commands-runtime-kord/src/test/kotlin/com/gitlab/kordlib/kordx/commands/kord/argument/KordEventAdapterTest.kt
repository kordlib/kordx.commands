package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.annotation.KordExperimental
import com.gitlab.kordlib.common.annotation.KordUnsafe
import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.common.entity.optional.Optional
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.Unsafe
import com.gitlab.kordlib.core.behavior.channel.MessageChannelBehavior
import com.gitlab.kordlib.core.entity.User
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.core.event.message.MessageUpdateEvent
import com.gitlab.kordlib.kordx.commands.argument.primitive.IntArgument
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter
import io.mockk.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@KordExperimental
@KordUnsafe
class KordEventAdapterTest {

    @Test
    fun `argument should be editable if previously rejected`() = runBlockingTest {
        val authorId = Snowflake(1L)
        val channelIdValue = Snowflake(2L)
        val messageIdValue = Snowflake(10L)

        val kordMock = mockk<Kord> {
            every { unsafe } returns Unsafe(this)
            every { resources.defaultStrategy.supply(this@mockk) } returns mockk()
        }
        val channelMock = mockk<MessageChannelBehavior> {
            every { id } returns channelIdValue
            coEvery { createMessage(any()) } returns mockk()
        }
        val authorMock = mockk<User> {
            every { id } returns authorId
        }

        val primaryEvent = mockk<MessageCreateEvent> {
            every { kord } returns kordMock
            every { message } returns mockk {
                every { author } returns authorMock
                every { channel } returns channelMock
            }
        }
        val incorrectValueEvent = mockk<MessageCreateEvent> {
            every { message } returns mockk {
                every { id } returns messageIdValue
                every { author } returns authorMock
                every { channel } returns channelMock
                every { content } returns "string"
            }
        }
        val updateEvent = mockk<MessageUpdateEvent> {
            every { kord } returns kordMock
            every { messageId } returns messageIdValue
            every { channelId } returns channelIdValue

            every { channel.id } returns channelIdValue
            every { old?.author } returns authorMock
            every { new.content } returns Optional("53")
        }

        every { kordMock.events } returns flowOf(
                incorrectValueEvent, incorrectValueEvent, updateEvent
        ).shareIn(this, SharingStarted.Lazily)

        val primaryAdapterEvent = KordEventAdapter(primaryEvent)
        val result = primaryAdapterEvent.read(IntArgument)

        coVerify(exactly = 2) { channelMock.createMessage("Expected a whole number.") }
        Assertions.assertEquals(53, result)
    }

}

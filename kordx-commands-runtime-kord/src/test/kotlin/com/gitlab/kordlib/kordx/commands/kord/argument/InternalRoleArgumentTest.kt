package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class InternalRoleArgumentTest {

    @Test
    fun `role matches against role mention`(): Unit = runBlocking {
        val roleId = 165511591545143296L
        val text = "sample <@&$roleId> text"
        val argument = RoleArgument
        val guildId = 556525343595298826L

        val kordMock: Kord = mockk {
            every { defaultSupplier } returns mockk()

            //You might have many questions about the validity and sanity of what is written here
            //fear not, it works. Unless you're here because it doesn't work. In which case: fear, a lot.
            coEvery { getGuild(Snowflake(any<Long>()), resources.defaultStrategy) } returns mockk {
                coEvery { getRole(Snowflake(any<Long>())) } returns mockk role@{
                    every { this@role.guildId } answers object : Answer<Any> {
                        override fun answer(call: Call): Any = guildId
                    } as Answer<Snowflake>

                    every { this@role.id } answers object : Answer<Any> {
                        override fun answer(call: Call): Any = roleId
                    } as Answer<Snowflake>
                }
            }
        }

        val result = argument.parse(text, 6, MessageCreateEvent(
                guildId = Snowflake(guildId),
                member = mockk(),
                message = mockk() {
                    every { kord } returns kordMock
                },
                supplier = kordMock.defaultSupplier,
                shard = 0
        ))

        require(result is ArgumentResult.Success) { "result is failure $result" }

        assertEquals(roleId, result.item.id.longValue)
        assertEquals(guildId, result.item.guildId.longValue)
    }

}
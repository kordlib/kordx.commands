package dev.kord.x.commands.kord.argument

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.x.commands.argument.result.ArgumentResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InternalRoleArgumentTest {

    @Test
    fun `role matches against role mention`() = runBlocking {
        val roleId = 165511591545143296L
        val text = "sample <@&$roleId> text"
        val argument = RoleArgument
        val guildId = 556525343595298826L

        val randGuild = Snowflake(guildId)
        val randRole = Snowflake(roleId)

        val kordMock: Kord = mockk {
            every { defaultSupplier } returns mockk()

            //You might have many questions about the validity and sanity of what is written here
            //fear not, it works. Unless you're here because it doesn't work. In which case: fear, a lot.
            coEvery { getGuild(randGuild, resources.defaultStrategy) } returns mockk {
                coEvery { getRoleOrNull(randRole) } returns mockk role@{
                    every { this@role.guildId } returns randGuild
                    every { this@role.id } returns randRole
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

        assertEquals(roleId, result.item.id.value)
        assertEquals(guildId, result.item.guildId.value)
    }

}

@file:dev.kordx.commands.annotation.AutoWired

package commands.example

import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kordx.commands.kord.plug.on
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

/**
 * greet a user once they join the guild
 */
val greetNewUser = on<MemberJoinEvent> {
    val textChannel = getGuild().channels.filterIsInstance<TextChannel>().first() //should probably have this configurable instead

    textChannel.createMessage("hello ${member.mention}!")
}

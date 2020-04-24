@file:AutoWired

package commands.example

import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.event.guild.MemberJoinEvent
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.kord.plug.on
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

/**
 * greet a user once they join the guild
 */
val greetNewUser = on<MemberJoinEvent> {
    val textChannel = getGuild().channels.filterIsInstance<TextChannel>().first() //should probably have this configurable instead

    textChannel.createMessage("hello ${member.nicknameMention}!")
}

package commands

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.entity.channel.TextChannel
import com.gitlab.kordlib.core.event.guild.MemberJoinEvent
import com.gitlab.kordlib.kordx.commands.argument.primitives.DoubleArgument
import com.gitlab.kordlib.kordx.commands.argument.text.WordArgument
import com.gitlab.kordlib.kordx.commands.command.Command
import com.gitlab.kordlib.kordx.commands.command.command
import com.gitlab.kordlib.kordx.commands.command.invoke
import com.gitlab.kordlib.kordx.commands.flow.toModifier
import com.gitlab.kordlib.kordx.commands.kord.*
import com.gitlab.kordlib.kordx.commands.kord.context.KordEventContext
import com.gitlab.kordlib.kordx.commands.kord.listeners.on
import com.gitlab.kordlib.kordx.emoji.Emojis
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.koin.core.get
import org.koin.dsl.module

class CommandSwitch(private val map: MutableMap<Command<*>, Boolean> = mutableMapOf()) {

    operator fun get(command: Command<*>): Boolean = map.getOrDefault(command, true)

    operator fun set(command: Command<*>, value: Boolean) {
        map[command] = value
    }

}

class UserResponses(val users: MutableMap<Snowflake, Boolean> = mutableMapOf())

val dependencyModule = module {
    single { CommandSwitch() }
    single { UserResponses() }
}

suspend fun main() = bot(System.getenv("token")) {
    koin {
        modules(dependencyModule)
    }

    prefix {
        add<Kord> { "+" }
    }

    addListener(greetNewUser)
    +guildOnly(get())
    +disableCommands(get())
    +toggleCommands(get())
    +simpleMath().toModifier("math")

}

val greetNewUser = on<MemberJoinEvent> {
    val textChannel = getGuild().channels.filterIsInstance<TextChannel>().first()

    textChannel.createMessage("hello ${member.nicknameMention}!")
}

fun guildOnly(responses: UserResponses) = eventFilter {
    (message.guildId != null).also {
        if (!it && responses.users[message.author!!.id] != true) {
            message.channel.createMessage("I only respond to guild messages.")
            responses.users[message.author!!.id] == true
        }
    }
}

fun disableCommands(switch: CommandSwitch) = precondition {
    switch[command].also {
        if (!it) respond("command is currently disabled")
    }
}

fun toggleCommands(switch: CommandSwitch) = module("command-control") {


    fun KordEventContext.getCommand(name: String): Command<*>? {
        val commands = command.modules.values.flatMap { it.commands.values }.map { it.name to it }.toMap()

        return commands[name]
    }

    command("disable") {

        invoke(WordArgument) {
            val command = getCommand(it) ?: return@invoke run {
                respond("no command with name $it found")
            }

            if (command.name == "disable" || command.name == "enable") return@invoke run {
                respond("can't disable that command")
            }

            switch[command] = false
            respond(Emojis.okHand.unicode)
        }

    }

    command("enable") {

        invoke(WordArgument) {
            val command = getCommand(it) ?: return@invoke run {
                respond("no command with name $it found")
            }

            if (command.name == "disable" || command.name == "enable") return@invoke run {
                respond("can't enable that command")
            }

            switch[command] = true
            respond(Emojis.okHand.unicode)
        }

    }

}

fun simpleMath() = commands {

    command("add") {

        invoke(DoubleArgument, DoubleArgument) { a, b ->
            respond("${a + b}")
        }

    }

}

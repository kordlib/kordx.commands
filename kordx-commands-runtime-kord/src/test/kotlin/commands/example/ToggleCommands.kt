@file:dev.kord.x.commands.annotation.AutoWired

package commands.example

//import com.gitlab.kordlib.kordx.emoji.Emojis
import dev.kord.x.commands.argument.extension.named
import dev.kord.x.commands.argument.text.WordArgument
import dev.kord.x.commands.kord.model.precondition.precondition
import dev.kord.x.commands.kord.module.module
import dev.kord.x.commands.model.command.Command
import dev.kord.x.commands.model.command.invoke
import org.koin.core.get

/**
 * register our CommandSwitch dependency
 */
val commandSwitchDependencies = org.koin.dsl.module {
    single { CommandSwitch() }
}

/**
 * container that keeps track of which commands are enabled/disabled
 */
class CommandSwitch(private val map: MutableMap<Command<*>, Boolean> = mutableMapOf()) {

    operator fun get(command: Command<*>): Boolean = map.getOrDefault(command, true)

    operator fun set(command: Command<*>, value: Boolean) {
        map[command] = value
    }

}

fun Command<*>.disable() = get<CommandSwitch>().set(this, false)
fun Command<*>.enable() = get<CommandSwitch>().set(this, true)
val Command<*>.isEnabled get() = get<CommandSwitch>()[this]

/**
 * Cancel commands that we've disabled with the [switch].
 */
fun ignoreDisabledCommands() = precondition {
    command.isEnabled.also {
        if (!it) respond("command is currently disabled")
    }
}

/**
 * commands to enable/disable commands
 */
fun toggleCommands() = module("command-control") {

    command("disable") {

        invoke(WordArgument.named("command")) {

            val command = commands[it] ?: return@invoke run {
                respond("no command with name $it found")
            }

            if (command.name == "disable" || command.name == "enable") return@invoke run {
                respond("can't disable that command")
            }

            command.disable()
            respond("\uD83D\uDC4C")
//            respond(Emojis.okHand.unicode)
        }

    }

    command("enable") {

        invoke(WordArgument.named("command")) {
            val command = commands[it] ?: return@invoke run {
                respond("no command with name $it found")
            }

            if (command.name == "disable" || command.name == "enable") return@invoke run {
                respond("can't enable that command")
            }

            command.enable()
            respond("\uD83D\uDC4C")
//            respond(Emojis.okHand.unicode)
        }

    }

}


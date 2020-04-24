@file:AutoWired

package commands.example

import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.argument.text.WordArgument
import com.gitlab.kordlib.kordx.commands.kord.model.precondition.precondition
import com.gitlab.kordlib.kordx.commands.model.command.Command
import com.gitlab.kordlib.kordx.commands.model.module.command
import com.gitlab.kordlib.kordx.commands.model.command.invoke
import  com.gitlab.kordlib.kordx.commands.kord.module.module
import com.gitlab.kordlib.kordx.emoji.Emojis
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

        invoke(WordArgument("command")) {

            val command = commands[it] ?: return@invoke run {
                respond("no command with name $it found")
            }

            if (command.name == "disable" || command.name == "enable") return@invoke run {
                respond("can't disable that command")
            }

            command.disable()
            respond(Emojis.okHand.unicode)
        }

    }

    command("enable") {

        invoke(WordArgument("command")) {
            val command = commands[it] ?: return@invoke run {
                respond("no command with name $it found")
            }

            if (command.name == "disable" || command.name == "enable") return@invoke run {
                respond("can't enable that command")
            }

            command.enable()
            respond(Emojis.okHand.unicode)
        }

    }

}


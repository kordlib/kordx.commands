@file:dev.kord.x.commands.annotation.AutoWired

package commands.example

import dev.kord.x.commands.annotation.AutoWired
import dev.kord.x.commands.annotation.ModuleName
import dev.kord.x.commands.argument.primitive.DoubleArgument
import dev.kord.x.commands.kord.module.commands
import dev.kord.x.commands.model.command.invoke

/**
 * example command that we can enable/disable.
 */
@dev.kord.x.commands.annotation.ModuleName("math")
fun simpleMath() = commands {

    command("add") {

        alias("+", "combine")

        invoke(DoubleArgument, DoubleArgument) { a, b ->
            respond("${a + b}")
        }

    }

}

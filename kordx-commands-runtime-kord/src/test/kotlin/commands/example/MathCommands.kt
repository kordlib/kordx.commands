@file:dev.kordx.commands.annotation.AutoWired

package commands.example

import dev.kordx.commands.annotation.AutoWired
import dev.kordx.commands.annotation.ModuleName
import dev.kordx.commands.argument.primitive.DoubleArgument
import dev.kordx.commands.kord.module.commands
import dev.kordx.commands.model.command.invoke

/**
 * example command that we can enable/disable.
 */
@dev.kordx.commands.annotation.ModuleName("math")
fun simpleMath() = commands {

    command("add") {

        alias("+", "combine")

        invoke(DoubleArgument, DoubleArgument) { a, b ->
            respond("${a + b}")
        }

    }

}

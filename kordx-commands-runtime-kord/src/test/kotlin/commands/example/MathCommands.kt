@file:AutoWired

package commands.example

import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.argument.primitives.DoubleArgument
import com.gitlab.kordlib.kordx.commands.command.command
import com.gitlab.kordlib.kordx.commands.command.invoke
import com.gitlab.kordlib.kordx.commands.kord.commands

/**
 * example command that we can enable disable.
 */
@ModuleName("math")
fun simpleMath() = commands {

    command("add") {

        invoke(DoubleArgument, DoubleArgument) { a, b ->
            respond("${a + b}")
        }

    }

}

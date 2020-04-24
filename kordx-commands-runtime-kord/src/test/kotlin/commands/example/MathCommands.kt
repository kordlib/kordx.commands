@file:AutoWired

package commands.example

import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.argument.primitives.DoubleArgument
import com.gitlab.kordlib.kordx.commands.model.module.command
import com.gitlab.kordlib.kordx.commands.kord.module.commands
import com.gitlab.kordlib.kordx.commands.model.command.invoke

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
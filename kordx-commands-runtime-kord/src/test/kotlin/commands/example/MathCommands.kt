@file:AutoWired

package commands.example

import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.argument.primitive.DoubleArgument
import com.gitlab.kordlib.kordx.commands.kord.module.commands
import com.gitlab.kordlib.kordx.commands.model.command.invoke

/**
 * example command that we can enable/disable.
 */
@ModuleName("math")
fun simpleMath() = commands {

    command("add", "+") {

        alias("combine")

        invoke(DoubleArgument, DoubleArgument) { a, b ->
            respond("${a + b}")
        }

    }

}

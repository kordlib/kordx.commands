import com.gitlab.kordlib.kordx.commands.annotation.CommandModule
import com.gitlab.kordlib.kordx.commands.argument.pipe.TestContext
import com.gitlab.kordlib.kordx.commands.command.module
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class KordProcessorTest {
    @Test
    @Disabled
    @CommandModule
    fun module() = module("kord stuff", TestContext) {

    }

}
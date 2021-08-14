package dev.kord.x.commands.argument.extension

import dev.kord.x.commands.argument.primitive.IntArgument
import dev.kord.x.commands.argument.requireFailure
import dev.kord.x.commands.argument.requireItem
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
class RepeatArgTest {

    val argument = IntArgument.repeated(1..10)

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly accepts arguments`(text: String, expected: List<Int>) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(expected)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun passingSources() = listOf(
                Arguments.of("1 2 3 4 5 6 7 8 9 10", listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of("")
        )
    }
}

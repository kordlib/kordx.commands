package dev.kordx.commands.argument.extension

import dev.kordx.commands.argument.primitive.IntArgument
import dev.kordx.commands.argument.requireFailure
import dev.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class RepeatArgTest {

    val argument = IntArgument.repeated(1..10)

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly accepts arguments`(text: String, expected: List<Int>) = runTest {
        argument.parse(text, 0, Unit).requireItem(expected)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runTest {
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
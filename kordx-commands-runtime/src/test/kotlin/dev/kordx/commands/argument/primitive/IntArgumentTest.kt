package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.requireFailure
import dev.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class IntArgumentTest {

    val argument = IntArgument

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly parses arguments`(text: String, result: Int) = runTest {
        argument.parse(text, 0, Unit).requireItem(result)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runTest {
        argument.parse(text, 0, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun passingSources() = listOf(
                Arguments.of("20", 20),
                Arguments.of("-5", -5),
                Arguments.of("0", 0),
                Arguments.of("2147483647", Int.MAX_VALUE)
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of("21474836471234898"),
                Arguments.of("15.3")
        )
    }

}
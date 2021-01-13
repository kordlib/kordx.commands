package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.requireFailure
import dev.kord.x.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class DoubleArgumentTest {

    val argument = DoubleArgument

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly parses arguments`(text: String, result: Double) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(result)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun passingSources() = listOf(
            Arguments.of("20", 20.0),
            Arguments.of("20.5", 20.5),
            Arguments.of("-50.3", -50.3),
            Arguments.of("5.0e-2", 5.0e-2)
        )

        @JvmStatic
        fun failingSources() = listOf(
            Arguments.of("a5238"),
            Arguments.of("A random sentence because why not")
        )
    }

}
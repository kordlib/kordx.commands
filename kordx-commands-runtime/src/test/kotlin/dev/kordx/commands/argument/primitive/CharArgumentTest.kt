package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.requireFailure
import dev.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class CharArgumentTest {

    val argument = CharArgument

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly parses arguments`(text: String, result: Char) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(result)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(text,0, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun passingSources() = listOf(
                Arguments.of("A", 'A'),
                Arguments.of("B", 'B'),
                Arguments.of("c", 'c'),
                Arguments.of("d", 'd')
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of("Three shalt be the number thou shalt count"),
                Arguments.of("and the number of the counting shall be three"),
                Arguments.of("Four shalt thou not count"),
                Arguments.of("char")
        )
    }

}
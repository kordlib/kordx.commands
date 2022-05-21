package dev.kordx.commands.argument.text

import dev.kordx.commands.argument.requireItem
import dev.kordx.commands.argument.requireSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

@Suppress("unused")
@ExperimentalCoroutinesApi
class StringArgumentTest {

    val argument = StringArgument

    @ParameterizedTest
    @MethodSource("sources")
    fun `correctly parses arguments`(text: String) = runTest {
        argument.parse(text, 0, Unit).requireItem(text)
    }

    @ParameterizedTest
    @MethodSource("sources")
    fun `correctly consumes all`(text: String) = runTest {
        val success = argument.parse(text, 5, Unit).requireSuccess()
        assertEquals(expected = text.length, actual = success.newIndex)
    }

    companion object {
        @JvmStatic
        fun sources() = listOf(
                Arguments.of("Tis but a scratch.")
        )
    }

}
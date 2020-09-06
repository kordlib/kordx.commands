package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.requireItem
import com.gitlab.kordlib.kordx.commands.argument.requireSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
    fun `correctly parses arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(text)
    }

    @ParameterizedTest
    @MethodSource("sources")
    fun `correctly consumes all`(text: String) = runBlockingTest {
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
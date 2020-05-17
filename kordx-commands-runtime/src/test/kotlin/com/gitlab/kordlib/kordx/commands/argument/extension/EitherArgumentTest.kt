package com.gitlab.kordlib.kordx.commands.argument.extension

import com.gitlab.kordlib.kordx.commands.argument.*
import com.gitlab.kordlib.kordx.commands.argument.primitive.DoubleArgument
import com.gitlab.kordlib.kordx.commands.argument.primitive.IntArgument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
class EitherArgumentTest {

    val argument = IntArgument or DoubleArgument

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly accepts arguments`(text: String, side: String, expected: Any) = runBlockingTest {
        val item = argument.parse(listOf(text), 0, Unit).requireSuccess().item
        when (side) {
            "left" -> Assertions.assertEquals(item.left, expected)
            "right" -> Assertions.assertEquals(item.right, expected)
        }
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(listOf(text), 0, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun passingSources() = listOf(
                Arguments.of("5", "left", 5),
                Arguments.of("10.0", "right", 10.0),
                Arguments.of("-5", "left", -5)
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of("not a number")
        )
    }
}
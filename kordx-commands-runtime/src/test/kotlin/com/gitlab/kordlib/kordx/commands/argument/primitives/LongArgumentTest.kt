package com.gitlab.kordlib.kordx.commands.argument.primitives

import com.gitlab.kordlib.kordx.commands.argument.requireFailure
import com.gitlab.kordlib.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource


@Suppress("unused")
@ExperimentalCoroutinesApi
class LongArgumentTest {

    val argument = LongArgument()

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly parses arguments`(text: String, result: Long) = runBlockingTest {
        argument.parse(text, Unit).requireItem(result)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(text, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun passingSources() = listOf(
                Arguments.of("20", 20L),
                Arguments.of("-5", -5L),
                Arguments.of("0", 0L),
                Arguments.of("9223372036854775807", Long.MAX_VALUE)
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of("9223372036854775807456875646"),
                Arguments.of("15.3")
        )
    }

}
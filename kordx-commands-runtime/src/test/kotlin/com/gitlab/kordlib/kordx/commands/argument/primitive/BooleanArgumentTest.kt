package com.gitlab.kordlib.kordx.commands.argument.primitive

import com.gitlab.kordlib.kordx.commands.argument.requireFailure
import com.gitlab.kordlib.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class BooleanArgumentTest {

    val argument = BooleanArgument()

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly parses arguments`(text: String, result: Boolean) = runBlockingTest {
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
                Arguments.of("true", true),
                Arguments.of("TrUe", true),
                Arguments.of("false", false),
                Arguments.of("FaLsE", false)
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of("cat"),
                Arguments.of("I'm clearly not a boolean"),
                Arguments.of("eslaf"),
                Arguments.of("uert")
        )
    }

}
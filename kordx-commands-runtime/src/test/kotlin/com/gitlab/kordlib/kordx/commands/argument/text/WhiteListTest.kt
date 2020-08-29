package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.requireFailure
import com.gitlab.kordlib.kordx.commands.argument.requireItem
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class WhiteListTest {

    val argument = WordArgument.whitelist("whitelist")

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly accepts arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(text)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireFailure()
    }


    companion object {
        @JvmStatic
        fun passingSources() = listOf(Arguments.of("whitelist"))

        @JvmStatic
        fun failingSources() = listOf(Arguments.of("false"))
    }
}
package com.gitlab.kordlib.kordx.commands.argument.text

import com.gitlab.kordlib.kordx.commands.argument.requireFailure
import com.gitlab.kordlib.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class WordArgumentTest {

    val argument = WordArgument

    @ParameterizedTest
    @MethodSource("sources")
    fun `correctly parses arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(text)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireFailure()
    }

    companion object {
        @JvmStatic
        fun sources() = listOf(
                Arguments.of("word")
        )

        @JvmStatic
        fun failingSources() = listOf(
                Arguments.of(""),
                Arguments.of(" "),
        )
    }

}
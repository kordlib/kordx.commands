package dev.kordx.commands.argument.text

import dev.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class WordsArgumentTest {

    val argument = WordsArgument

    @ParameterizedTest
    @MethodSource("sources")
    fun `correctly parses arguments`(text: String) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(text.split(" "))
    }

    companion object {
        @JvmStatic
        fun sources() = listOf(
                Arguments.of("Tis but a scratch.")
        )
    }

}
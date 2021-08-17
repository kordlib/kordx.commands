package dev.kord.x.commands.argument.text

import dev.kord.x.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("unused")
@ExperimentalCoroutinesApi
class ListArgumentTest {

    @ParameterizedTest
    @MethodSource("sources")
    fun `correctly parses arguments`(separator: String, text: String, result: List<String>) =
        runBlockingTest {
            ListArgument(separator = separator).parse(text, 0, Unit).requireItem(result)
        }

    companion object {
        @JvmStatic
        fun sources() = listOf(
            Arguments.of("|", "hello | world", listOf("hello ", " world")),
            Arguments.of(
                ",",
                "Three shall be the number thou shalt count, and the number of the counting shall be three.",
                listOf(
                    "Three shall be the number thou shalt count",
                    " and the number of the counting shall be three."
                )
            ),
            Arguments.of(".", "one.two.three", listOf("one", "two", "three")),
            Arguments.of(".", "one..two..three", listOf("one", "", "two", "", "three"))
        )
    }

}

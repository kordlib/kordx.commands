@file:Suppress("unused")

package dev.kordx.commands.argument.primitive

import dev.kordx.commands.argument.extension.inRange
import dev.kordx.commands.argument.requireFailure
import dev.kordx.commands.argument.requireItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExperimentalCoroutinesApi
class InRangeTest {

    val argument = IntArgument.inRange(range)

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly accepts arguments`(text: String, result: Int) = runTest {
        argument.parse(text, 0, Unit).requireItem(result)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runTest {
        argument.parse(text, 0, Unit).requireFailure()
    }


    companion object {
        val range = -5..5

        @JvmStatic
        fun passingSources() = range.map { Arguments.of(it.toString(), it) }

        @JvmStatic
        fun failingSources() =((-10..10) - range).map { Arguments.of(it.toString()) }
    }
}

@file:Suppress("unused")

package dev.kord.x.commands.argument.primitive

import dev.kord.x.commands.argument.extension.inRange
import dev.kord.x.commands.argument.requireFailure
import dev.kord.x.commands.argument.requireItem
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class InRangeTest {

    val argument = IntArgument.inRange(range)

    @ParameterizedTest
    @MethodSource("passingSources")
    fun `correctly accepts arguments`(text: String, result: Int) = runBlockingTest {
        argument.parse(text, 0, Unit).requireItem(result)
    }

    @ParameterizedTest
    @MethodSource("failingSources")
    fun `correctly fails arguments`(text: String) = runBlockingTest {
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

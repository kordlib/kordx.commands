package dev.kordx.commands.kord.argument

import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.kord.argument.CodeBlockArgument
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class CodeBlockArgumentTest {

    @Test
    fun `argument parses properly formatted code block entirely`(): Unit = runBlocking {
        val argument = CodeBlockArgument

        val language = "kotlin"
        val content = """
            println("people always ask:")
            println("Where is the next release?")
            println("But they never ask:")
            println("How is the next release?")
        """

        val input = """
            ```$language
            ${content.trim()}
            ```
        """.trimIndent()

        val result = argument.parse(input, 0, Unit)

        assertTrue(result is ArgumentResult.Success) { "expected success but was $result" }
        result as ArgumentResult.Success

        assertEquals(input.length, result.newIndex)
    }

    @Test
    fun `argument parses properly formatted code block`(): Unit = runBlocking {
        val argument = CodeBlockArgument

        val language = "kotlin"
        val content = """
            println("people always ask:")
            println("Where is the next release?")
            println("But they never ask:")
            println("How is the next release?")
        """

        val result = argument.parse("""
            ```$language
            ${content.trim()}
            ```
        """.trimIndent(), 0, Unit)

        assertTrue(result is ArgumentResult.Success) { "expected success but was $result" }
        result as ArgumentResult.Success

        assertEquals(language, result.item.language)
        assertEquals(content.trimIndent() + "\n", result.item.content)
    }

    @Test
    fun `argument parses poorly spaced language as content`(): Unit = runBlocking {
        val argument = CodeBlockArgument

        val language = " kotlin "
        val content = """
            println("people always ask:")
            println("Where is the next release?")
            println("But they never ask:")
            println("How is the next release?")
        """

        val result = argument.parse("""
            ```$language
            ${content.trim()}
            ```
        """.trimIndent(), 0, Unit)

        assertTrue(result is ArgumentResult.Success) { "expected success but was $result" }
        result as ArgumentResult.Success

        assertEquals(null, result.item.language)
        assertEquals(language + "\n" + content.trimIndent() + "\n", result.item.content)
    }

    @Test
    fun `argument parses non-language as content`(): Unit = runBlocking {
        val argument = CodeBlockArgument

        val language = "kotlin script"
        val content = """
            println("people always ask:")
            println("Where is the next release?")
            println("But they never ask:")
            println("How is the next release?")
        """

        val result = argument.parse("""
            ```$language
            ${content.trim()}
            ```
        """.trimIndent(), 0, Unit)

        assertTrue(result is ArgumentResult.Success) { "expected success but was $result" }
        result as ArgumentResult.Success

        assertEquals(null, result.item.language)
        assertEquals(language + "\n" + content.trimIndent() + "\n", result.item.content)
    }

    @Test
    fun `argument parses only-language as content`(): Unit = runBlocking {
        val argument = CodeBlockArgument

        val language = "kotlin"

        val result = argument.parse("""
            ```$language```
        """.trimIndent(), 0, Unit)

        assertTrue(result is ArgumentResult.Success) { "expected success but was $result" }
        result as ArgumentResult.Success

        assertEquals(null, result.item.language)
        assertEquals(language, result.item.content)
    }


}
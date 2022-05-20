package dev.kordx.processor

import com.squareup.kotlinpoet.FunSpec
import javax.lang.model.element.ExecutableElement
import dev.kordx.commands.model.processor.ProcessorBuilder

/**
 * Builds a code block that calls the [resolved] version of the [values] in a list and assigns them to a variable
 * with the given [name].
 */
inline fun FunSpec.Builder.list(
        name: String,
        values: Set<ExecutableElement>,
        crossinline append: (ExecutableElement) -> String = { "" }
) {
    if (values.isEmpty()) return
    val joined = values.joinToString(",") { "${it.resolved}${append(it)}" }
    addStatement("$name += listOf($joined)")

}

/**
 * Builds a code block that calls the [resolved] version of the [values] in a list and adds them to a
 * [ProcessorBuilder].
 */
inline fun FunSpec.Builder.plug(
        values: Set<ExecutableElement>,
        crossinline append: (ExecutableElement) -> String = { "" }
) {
    if (values.isEmpty()) return
    values.forEach {
        val resolved = "${it.resolved}${append(it)}"
        addStatement("addPlug($resolved)")
    }
}

/**
 * Builds a set of statements that calls the [resolved] version of the [handlers] and adds them to a
 * [ProcessorBuilder].
 */
fun FunSpec.Builder.eventHandlers(handlers: Set<ExecutableElement>) {
    handlers.forEach {
        addStatement("+${it.resolved}")
    }
}

/**
 * Builds a code block that calls the [resolved] version of the [prefixes] and adds them to a
 * [ProcessorBuilder].
 */
fun FunSpec.Builder.prefixes(prefixes: Set<ExecutableElement>) {
    val applying = prefixes.joinToString("\n") {
        "${it.resolved}.apply(this)"
    }
    if (applying.isEmpty()) return
    addStatement("""
            prefix {
                $applying
            }
        """.trimIndent())
}

/**
 * Builds a code block that calls the [resolved] version of the [modules] and adds them to a
 * [ProcessorBuilder].
 */
fun FunSpec.Builder.koin(modules: Set<ExecutableElement>) {
    addStatement("""
        koin {
            modules(listOf(${modules.joinToString(",") { it.resolved }}))
        }
        """.trimIndent())
}

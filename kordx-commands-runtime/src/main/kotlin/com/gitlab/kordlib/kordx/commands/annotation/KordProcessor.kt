package com.gitlab.kordlib.kordx.commands.annotation

import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder
import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.flow.ModuleModifier
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.pipe.EventHandler
import com.gitlab.kordlib.kordx.commands.pipe.EventSource
import com.gitlab.kordlib.kordx.commands.pipe.Prefix
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyCommandModule


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyModuleModifier

@Target(AnnotationTarget.FUNCTION)
annotation class SupplyEventFilter


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyEventHandler

@Target(AnnotationTarget.FUNCTION)
annotation class SupplyEventSource

@Target(AnnotationTarget.FUNCTION)
annotation class SupplyPrecondition


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyPrefix

@AutoService(Processor::class)
class KordProcessor : AbstractProcessor() {

    override fun process(p0: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        FunSpec.builder("configure").apply {
            val modules = getAnnotations<ModuleBuilder<*, *, *>>(SupplyCommandModule::class.java, env)
            list("moduleGenerators", modules) { "${it.qualifiedName}().toGenerator()" }

            val moduleModifiers = getAnnotations<ModuleModifier>(SupplyModuleModifier::class.java, env)
            list("moduleModifiers", moduleModifiers)

            val eventFilters = getAnnotations<EventFilter<*>>(SupplyEventFilter::class.java, env)
            list("eventFilters", eventFilters)

            val eventSources = getAnnotations<EventSource<*>>(SupplyEventSource::class.java, env)
            list("eventSources", eventSources)

            val prefixes = getAnnotations<Prefix<*, *, *>>(SupplyPrefix::class.java, env)
            list("prefixes", prefixes)

            val preconditions = getAnnotations<Precondition<*>>(SupplyPrecondition::class.java, env)
            list("preconditions", preconditions)

            val eventHandler = getAnnotations<EventHandler>(SupplyEventHandler::class.java, env)
            variable("eventHandler", eventHandler)
            build()

        }
        return true
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(
            SupplyCommandModule::class.java.name,
            SupplyModuleModifier::class.java.name,
            SupplyEventSource::class.java.name,
            SupplyEventFilter::class.java.name,
            SupplyEventHandler::class.java.name,
            SupplyPrecondition::class.java.name
    )

    override fun getSupportedSourceVersion() = SourceVersion.latest()!!

    private inline fun <reified R> getAnnotations(annotation: Class<out Annotation>, env: RoundEnvironment): Set<ExecutableElement> {
        val elements = env.getElementsAnnotatedWith(annotation).map { it as TypeElement }
        check(elements.all { it.returnType is R }) { "functions annotated with ${annotation.name} must return ${R::class.java.canonicalName}" }
        return elements
    }

}

private fun FunSpec.Builder.list(name: String, values: Set<ExecutableElement>, join: (TypeElement) -> String = { "${it.qualifiedName}()" }) {
    val functions = values.map { it as TypeElement }
    val joint = functions.joinToString(",") { join(it) }
    addStatement("$name += listOf($joint)")

}


fun FunSpec.Builder.variable(name: String, values: Set<ExecutableElement>, value: (TypeElement) -> String = { "${it.qualifiedName}()" }) {
    val functions = values.map { it as TypeElement }
    addStatement("$name = ${value(functions.last())}()")
}

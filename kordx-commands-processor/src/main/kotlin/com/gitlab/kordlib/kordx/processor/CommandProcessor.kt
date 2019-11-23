package com.gitlab.kordlib.kordx.processor

import com.gitlab.kordlib.kordx.commands.flow.EventFilter
import com.gitlab.kordlib.kordx.commands.flow.ModuleGenerator
import com.gitlab.kordlib.kordx.commands.flow.ModuleModifier
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.pipe.EventHandler
import com.gitlab.kordlib.kordx.commands.pipe.EventSource
import com.gitlab.kordlib.kordx.commands.pipe.PipeConfig
import com.gitlab.kordlib.kordx.commands.pipe.Prefix
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

@AutoService(Processor::class)
class CommandProcessor : AbstractProcessor() {

    override fun process(p0: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        val path = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()
        val file = File(path)
        file.delete()
        println("KordProcessor is here to save the day")
        val function = FunSpec.builder("configure").apply {
            receiver(PipeConfig::class)
            val modules = getFunctionsAnnotatedWith(SupplyCommandModule::class.java, env)
            val moduleModifiers = getFunctionsAnnotatedWith(SupplyModuleModifier::class.java, env)
            val eventFilters = getFunctionsAnnotatedWith(SupplyEventFilter::class.java, env)
            val prefixes = getFunctionsAnnotatedWith(SupplyPrefix::class.java, env)
            val eventSources = getFunctionsAnnotatedWith(SupplyEventSource::class.java, env)
            val preconditions = getFunctionsAnnotatedWith(SupplyPrecondition::class.java, env)
            val eventHandler = getFunctionsAnnotatedWith(SupplyEventHandler::class.java, env)
            if (modules.isEmpty()
                    && moduleModifiers.isEmpty()
                    && eventFilters.isEmpty()
                    && prefixes.isEmpty()
                    && eventSources.isEmpty()
                    && preconditions.isEmpty()
                    && eventFilters.isEmpty()
                    && eventHandler.isEmpty()) return true
            list<ModuleGenerator>(processingEnv, "moduleGenerators", modules) { ".toGenerator()" }
            list<ModuleModifier>(processingEnv, "moduleModifiers", moduleModifiers)
            list<EventFilter<*>>(processingEnv, "eventFilters", eventFilters)
            list<EventSource<*>>(processingEnv, "eventSources", eventSources)
            list<Prefix<*, *, *>>(processingEnv, "prefixes", prefixes)
            list<Precondition<*>>(processingEnv, "preconditions", preconditions)
            variable<EventHandler>(processingEnv, "eventHandler", eventHandler)


        }.build()
        file.mkdir()
        FileSpec.builder(KAPT_KOTLIN_GENERATED_OPTION_NAME, "Generated_Configuration")
                .addImport("com.gitlab.kordlib.kordx.commands.flow", "toGenerator")
                .addFunction(function)
                .build()
                .writeTo(file)
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

    fun getFunctionsAnnotatedWith(annotation: Class<out Annotation>, env: RoundEnvironment): Set<ExecutableElement> {
        val elements = env.getElementsAnnotatedWith(annotation)
        val functions = elements.filterIsInstance<ExecutableElement>()
        return functions.toSet()
    }

}

inline fun <reified R> FunSpec.Builder.list(env: ProcessingEnvironment,
                                            name: String, values: Set<ExecutableElement>,
                                            crossinline join: (ExecutableElement) -> String = { "" }) {
    if (values.isEmpty()) return
    checkType<R>(values, env)
    val joint = values.joinToString(",") {
        val absName = it.qualifiedName(env)
        "$absName()${join(it)}"
    }
    addStatement("$name += listOf($joint)")

}

inline fun <reified R> checkType(c: Collection<ExecutableElement>, env: ProcessingEnvironment) {
    val qualified = R::class.java.canonicalName
    val expected = env.elementUtils.getTypeElement(qualified).asType()
    for (f in c) {
        check(env.typeUtils.isSameType(f.returnType, expected)) { "${f.qualifiedName(env)} must return $qualified" }
    }

}

inline fun <reified R> FunSpec.Builder.variable(env: ProcessingEnvironment, name: String, values: Set<ExecutableElement>) {
    if (values.isEmpty()) return
    checkType<R>(values, env)
    val qualifiedName = values.first().qualifiedName(env)
    addStatement("$name = $qualifiedName()")
}

fun ExecutableElement.qualifiedName(env: ProcessingEnvironment): String {
    val pack = env.elementUtils.getPackageOf(this)
    return if (pack.isUnnamed) simpleName.toString()
    else "$pack.$simpleName"

}
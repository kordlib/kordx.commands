package com.gitlab.kordlib.kordx.commands.annotation

import com.gitlab.kordlib.kordx.commands.flow.ModuleGenerator
import com.gitlab.kordlib.kordx.commands.pipe.PipeConfig
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
import javax.tools.Diagnostic

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

@Target(AnnotationTarget.FUNCTION)
annotation class Command


@Target(AnnotationTarget.FUNCTION)
annotation class CommandModule


@Target(AnnotationTarget.FUNCTION)
annotation class ModuleModifier

@Target(AnnotationTarget.FUNCTION)
annotation class EventFilter


@Target(AnnotationTarget.FUNCTION)
annotation class EventHandler

@Target(AnnotationTarget.FUNCTION)
annotation class EventSource

@Target(AnnotationTarget.FUNCTION)
annotation class Precondition


@Target(AnnotationTarget.FUNCTION)
annotation class Prefix

@AutoService(Processor::class)
class KordProcessor : AbstractProcessor() {

    override fun process(p0: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment): Boolean {

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()
        val configuration = with(FunSpec.builder("configure")) {
            receiver(PipeConfig::class)
            listFromAnnotatedElements<ModuleGenerator>("moduleGenerators", CommandModule::class.java, roundEnvironment, processingEnv)
            listFromAnnotatedElements<com.gitlab.kordlib.kordx.commands.flow.ModuleModifier>("moduleModifiers", ModuleModifier::class.java, roundEnvironment, processingEnv)
            listFromAnnotatedElements<com.gitlab.kordlib.kordx.commands.pipe.EventSource<*>>("eventSources", EventSource::class.java, roundEnvironment, processingEnv)
            listFromAnnotatedElements<com.gitlab.kordlib.kordx.commands.flow.EventFilter<*>>("eventFilters", EventFilter::class.java, roundEnvironment, processingEnv)
            listFromAnnotatedElements<com.gitlab.kordlib.kordx.commands.flow.Precondition<*>>("preconditions", Precondition::class.java, roundEnvironment, processingEnv)
            listFromAnnotatedElements<com.gitlab.kordlib.kordx.commands.pipe.Prefix<*, *, *>>("prefixes", Prefix::class.java, roundEnvironment, processingEnv)
            build()
        }
        FileSpec.builder("com.gitlab.kordlib.kordx.commands.generated", "Generated_Configuration")
                .addFunction(configuration)
                .build()
                .writeTo(File(kaptKotlinGeneratedDir))
        return true
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(
            Command::class.java.name,
            CommandModule::class.java.name,
            ModuleModifier::class.java.name,
            EventSource::class.java.name,
            EventFilter::class.java.name,
            EventHandler::class.java.name,
            Precondition::class.java.name
    )

    override fun getSupportedSourceVersion() = SourceVersion.latest()


}

inline fun <reified T> FunSpec.Builder.listFromAnnotatedElements(variable: String, annotation: Class<out Annotation>, roundEnvironment: RoundEnvironment, processingEnvironment: ProcessingEnvironment) {
    val functions = roundEnvironment.getElementsAnnotatedWith(annotation)
    val ifAllReturnT = functions.all { (it as ExecutableElement).returnType is T }
    if (!ifAllReturnT) {
        processingEnvironment.messager.printMessage(Diagnostic.Kind.ERROR,
                "A function anotated with ${annotation.name} must return ${T::class.java.name}")
    }
    val absoluteNames = functions.joinToString(",") {
        val pack = processingEnvironment.elementUtils.getPackageOf(it).toString()
        val name = it.simpleName.toString()
        "$pack.$name"
    }
    addStatement("$variable += listOf($absoluteNames)")
}
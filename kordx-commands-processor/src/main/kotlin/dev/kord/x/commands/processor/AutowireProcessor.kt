@file:Suppress("MemberVisibilityCanBePrivate")

package dev.kord.x.commands.processor

import dev.kord.x.commands.annotation.AutoWired
import dev.kord.x.commands.model.eventFilter.EventFilter
import dev.kord.x.commands.model.module.CommandSet
import dev.kord.x.commands.model.module.ModuleModifier
import dev.kord.x.commands.model.plug.Plug
import dev.kord.x.commands.model.precondition.Precondition
import dev.kord.x.commands.model.prefix.PrefixConfiguration
import dev.kord.x.commands.model.processor.EventHandler
import dev.kord.x.commands.model.processor.EventSource
import dev.kord.x.commands.model.processor.ProcessorBuilder
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import dev.kord.x.commands.annotation.ModuleName
import org.koin.core.module.Module
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic
import kotlin.reflect.typeOf

internal const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
internal const val KEY_VERBOSE = "kordx.commands.verbose"

internal fun ProcessingEnvironment.getOption(key: String): Boolean = when (options[key]) {
    "", "true", "True" -> true
    else -> false
}

/**
 * Processor that creates a `configure` extension function for a [ProcessorBuilder], automatically
 * setting up static [AutoWired] members.
 */
@ExperimentalStdlibApi
@AutoService(Processor::class)
class CommandProcessor : AbstractProcessor() {
    private var firstRun = true

    override fun getSupportedOptions(): MutableSet<String> = mutableSetOf("kordx.commands.verbose")

    private val verbose get() = processingEnv.getOption(KEY_VERBOSE)

    private fun PipeItems.log(env: ProcessingEnvironment) {
        if (!verbose) return

        val kind = Diagnostic.Kind.WARNING

        with(env.messager) {
            printMessage(kind, "commandSets: $commandSets")
            printMessage(kind, "filters: $filters")
            printMessage(kind, "handlers: $handlers")
            printMessage(kind, "koins: $koins")
            printMessage(kind, "modules: $modules")
            printMessage(kind, "plugs: $plugs")
            printMessage(kind, "precondtions: $preconditions")
            printMessage(kind, "prefixes: $prefixes")
            printMessage(kind, "sources: $sources")
        }
    }

    override fun process(p0: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        val items = getAutoWired(env)
        items.log(processingEnv)

        if (items.isEmpty() && firstRun) { //first time we ran this and we didn't get anything to autowire, better warn
            processingEnv.messager.printMessage(
                Diagnostic.Kind.WARNING,
                "no autowireable entities found"
            )
        }

        if (items.isEmpty() && !firstRun) { //consecutive runs didn't find anything else, we're fine
            return true
        }

        val function = FunSpec.builder("configure").apply {
            addModifiers(KModifier.SUSPEND, KModifier.INLINE)
            receiver(ProcessorBuilder::class)

            koin(items.koins)

            list("moduleModifiers", items.modules)
            list("eventFilters", items.filters)
            list("eventSources", items.sources)
            list("moduleModifiers", items.commandSets) { ".toModifier(\"${it.moduleName()}\")" }
            list("preconditions", items.preconditions)
            plug(items.plugs)
            eventHandlers(items.handlers)
            prefixes(items.prefixes)
        }.build()

        val path = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]!!
        val file = File(path)

        with(FileSpec.builder(KAPT_KOTLIN_GENERATED_OPTION_NAME, "Generated_Configuration")) {
            addImport("dev.kord.x.commands.model.module", "toModifier")
            addImport("org.koin.core", "get")
            fun Iterable<ExecutableElement>.import() = forEach {
                val packageName = processingEnv.elementUtils.getPackageOf(it).qualifiedName
                addImport(packageName.toString(), it.simpleKotlinName)
            }

            items.handlers.import()
            items.preconditions.import()
            items.filters.import()
            items.sources.import()
            items.modules.import()
            items.koins.import()
            items.commandSets.import()
            items.prefixes.import()
            items.plugs.import()
            addFunction(function)

        }.build().writeTo(file)
        firstRun = false
        return true
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        AutoWired::class.java.name,
        ModuleName::class.java.name
    )

    private fun ExecutableElement.moduleName(): String =
        getAnnotation(ModuleName::class.java).name

    /**
     * returns the wildcard type mirror of [T]
     */
    private inline fun <reified T> typeMirror(): TypeMirror {
        val nonErasedType =
            processingEnv.elementUtils.getTypeElement(T::class.java.canonicalName).asType()
        return processingEnv.typeUtils.erasure(nonErasedType)
    }

    private infix fun TypeMirror.isAssignableTo(other: TypeMirror) =
        processingEnv.typeUtils.isAssignable(this, other)

    private fun getAutoWired(env: RoundEnvironment): PipeItems {
        val moduleModifierType = typeMirror<ModuleModifier>()
        val eventSourceType = typeMirror<EventSource<*>>()
        val eventHandlerType = typeMirror<EventHandler<*>>()
        val filterType = typeMirror<EventFilter<*>>()
        val koinType = typeMirror<Module>()
        val preconditionType = typeMirror<Precondition<*>>()
        val commandSetType = typeMirror<CommandSet>()
        val prefixesType = typeMirror<PrefixConfiguration>()
        val plugType = typeMirror<Plug>()

        val elements =
            env.getElementsAnnotatedWith(AutoWired::class.java)
        val functions = elements.flatMap {
            if (it.isClass) {
                it.asClass.enclosedElements.filterIsInstance<ExecutableElement>()
                    .filter { element -> Modifier.STATIC in element.modifiers }
            } else listOf(it).filterIsInstance<ExecutableElement>()
        }

        val items = PipeItems()
        functions.forEach { item ->
            val returnType = processingEnv.typeUtils.erasure(item.returnType)

            when {
                returnType isAssignableTo commandSetType ->
                    items.commandSets.add(item.ensureAnnotatedWith<ModuleName>())
                returnType isAssignableTo moduleModifierType -> items.modules.add(item)
                returnType isAssignableTo eventSourceType -> items.sources.add(item)
                returnType isAssignableTo eventHandlerType -> items.handlers.add(item)
                returnType isAssignableTo filterType -> items.filters.add(item)
                returnType isAssignableTo preconditionType -> items.preconditions.add(item)
                returnType isAssignableTo koinType -> items.koins.add(item)
                returnType isAssignableTo prefixesType -> items.prefixes.add(item)
                returnType isAssignableTo plugType -> items.plugs.add(item)
                else -> {
                    val message = "ignoring $item with unknown return type: $returnType"
                    processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, message)
                }
            }

        }

        return items
    }

    private inline fun <reified T : Annotation> ExecutableElement.ensureAnnotatedWith(): ExecutableElement {
        require(this.getAnnotation(T::class.java) != null) {
            val message = "$this needs to be annotated with ${typeOf<T>()}"
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
            message
        }

        return this
    }

    override fun getSupportedSourceVersion() = SourceVersion.latest()!!

}

@file:Suppress("MemberVisibilityCanBePrivate")

package com.gitlab.kordlib.kordx.processor

import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.model.eventFilter.EventFilter
import com.gitlab.kordlib.kordx.commands.model.module.CommandSet
import com.gitlab.kordlib.kordx.commands.model.module.ModuleModifier
import com.gitlab.kordlib.kordx.commands.model.plug.Plug
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.model.prefix.PrefixConfiguration
import com.gitlab.kordlib.kordx.commands.model.processor.EventHandler
import com.gitlab.kordlib.kordx.commands.model.processor.EventSource
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorConfig
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import org.koin.core.module.Module
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic
import kotlin.reflect.typeOf

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
const val KEY_VERBOSE = "kordx.commands.verbose"

fun ProcessingEnvironment.getOption(key: String): Boolean = when (options[key]) {
    "", "true", "True" -> true
    else -> false
}

@ExperimentalStdlibApi
@AutoService(Processor::class)
class CommandProcessor : AbstractProcessor() {
    var firstRun = true

    override fun getSupportedOptions(): MutableSet<String> = mutableSetOf("kordx.commands.verbose")

    val verbose get() = processingEnv.getOption(KEY_VERBOSE)

    fun PipeItems.log(env: ProcessingEnvironment) {
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
            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "no autowireable entities found")
        }

        if (items.isEmpty() && !firstRun) { //consecutive runs didn't find anything else, we're fine
            //TODO: Eventually we want to store the items from each consecutive run, compare them, and generate if needed
            return true
        }

        val function = FunSpec.builder("configure").apply {
            addModifiers(KModifier.SUSPEND, KModifier.INLINE)
            receiver(ProcessorConfig::class)

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
            addImport("com.gitlab.kordlib.kordx.commands.model.module", "toModifier")
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

    fun ExecutableElement.moduleName(): String = getAnnotation(ModuleName::class.java).name


    /**
     * returns the wildcard type mirror of [T]
     */
    private inline fun <reified T> typeMirror() = processingEnv.typeUtils.erasure(processingEnv.elementUtils.getTypeElement(T::class.java.canonicalName).asType())

    infix fun TypeMirror.isAssignableTo(other: TypeMirror) = processingEnv.typeUtils.isAssignable(this, other)

    fun getAutoWired(env: RoundEnvironment): PipeItems {
        val moduleModifierType = typeMirror<ModuleModifier>()
        val eventSourceType = typeMirror<EventSource<*>>()
        val eventHandlerType = typeMirror<EventHandler<*>>()
        val filterType = typeMirror<EventFilter<*>>()
        val koinType = typeMirror<Module>()
        val preconditionType = typeMirror<Precondition<*>>()
        val commandSetType = typeMirror<CommandSet>()
        val prefixesType = typeMirror<PrefixConfiguration>()
        val plugType = typeMirror<Plug<*>>()

        val elements = env.getElementsAnnotatedWith(AutoWired::class.java)
        val functions = elements.flatMap {
            if (it.isClass) {
                it.asClass.enclosedElements.filterIsInstance<ExecutableElement>().filter { Modifier.STATIC in it.modifiers }
            } else listOf(it).filterIsInstance<ExecutableElement>()
        }

        val items = PipeItems()
        functions.forEach { item ->
            val returnType = processingEnv.typeUtils.erasure(item.returnType)

            when {
                returnType isAssignableTo moduleModifierType -> items.modules.add(item)
                returnType isAssignableTo commandSetType -> items.commandSets.add(item.ensureAnnotatedWith<ModuleName>())
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

    val Element.isClass get() = this is TypeElement

    val Element.asClass get() = this as TypeElement

    inline fun FunSpec.Builder.list(
            name: String, values: Set<ExecutableElement>,
            crossinline append: (ExecutableElement) -> String = { "" }
    ) {
        if (values.isEmpty()) return
        val joined = values.joinToString(",") { "${it.resolved}${append(it)}" }
        addStatement("$name += listOf($joined)")

    }

    inline fun FunSpec.Builder.plug(
            values: Set<ExecutableElement>,
            crossinline append: (ExecutableElement) -> String = { "" }
    ) {
        if (values.isEmpty()) return
        val joined = values.joinToString(",") { "${it.resolved}${append(it)}" }
        addStatement("addPlugs(listOf($joined))")
    }

    fun FunSpec.Builder.eventHandlers(handlers: Set<ExecutableElement>) {
        handlers.forEach {
            addStatement("+${it.resolved}")
        }
    }

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

    fun FunSpec.Builder.koin(modules: Set<ExecutableElement>) {
        addStatement("""
        koin {
            modules(listOf(${modules.joinToString(",") { it.resolved }}))
        }
        """.trimIndent())
    }

    val ExecutableElement.isProperty get() = simpleName.startsWith("get") && parameters.size == 0

    val ExecutableElement.simpleKotlinName
        get() = when (isProperty) {
            true -> simpleName.toString().removePrefix("get").decapitalize()
            false -> simpleName.toString()
        }

    val ExecutableElement.resolved: String
        get() = when (isProperty) {
            true -> simpleName.toString().removePrefix("get").decapitalize()
            else -> "${simpleName}(${parameters.joinToString(",") { "get()" }})" //functionCall(get(),get(),...)
        }
}

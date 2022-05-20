package dev.kordx.commands.annotation

import dev.kordx.commands.model.processor.ProcessorBuilder
import dev.kordx.commands.model.processor.EventHandler

/**
 * Will mark this element for automatic registry to the [ProcessorBuilder] using the annotation processor,
 * suspending functions are supported.
 * Functions and properties annotated with [AutoWired] need to return one of the following types:
 * * [org.koin.core.module.Module]
 * * [dev.kordx.commands.model.module.ModuleModifier]
 * * [dev.kordx.commands.model.processor.EventSource]
 * * [dev.kordx.commands.model.processor.EventHandler]
 * * [dev.kordx.commands.model.eventFilter.EventFilter]
 * * [dev.kordx.commands.model.precondition.Precondition]
 * * [dev.kordx.commands.model.module.CommandSet] (requires [ModuleName])
 * * [dev.kordx.commands.model.prefix.PrefixConfiguration]
 * * [dev.kordx.commands.model.plug.Plug]
 *
 * > The target can also be applied to files, in which case the annotation processor will act as if every
 * > function or property with the above mentioned return types would be annotated with [AutoWired]
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FILE)
annotation class AutoWired

/**
 * Annotation that specifies the module [name] for command sets.
 * This annotation is required for [AutoWired] [EventHandlers][EventHandler].
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class ModuleName(val name: String)

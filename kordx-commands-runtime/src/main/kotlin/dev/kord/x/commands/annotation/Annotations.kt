package dev.kord.x.commands.annotation

import dev.kord.x.commands.model.processor.EventHandler
import dev.kord.x.commands.model.processor.ProcessorBuilder

/**
 * Will mark this element for automatic registry to the [ProcessorBuilder] using the annotation processor,
 * suspending functions are supported.
 * Functions and properties annotated with [AutoWired] need to return one of the following types:
 * * [org.koin.core.module.Module]
 * * [dev.kord.x.commands.model.module.ModuleModifier]
 * * [dev.kord.x.commands.model.processor.EventSource]
 * * [dev.kord.x.commands.model.processor.EventHandler]
 * * [dev.kord.x.commands.model.eventFilter.EventFilter]
 * * [dev.kord.x.commands.model.precondition.Precondition]
 * * [dev.kord.x.commands.model.module.CommandSet] (requires [ModuleName])
 * * [dev.kord.x.commands.model.prefix.PrefixConfiguration]
 * * [dev.kord.x.commands.model.plug.Plug]
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

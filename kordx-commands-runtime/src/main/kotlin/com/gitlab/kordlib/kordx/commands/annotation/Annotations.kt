package com.gitlab.kordlib.kordx.commands.annotation

import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorConfig

/**
 * Will mark this element for automatic registry to the [ProcessorConfig] using the annotation processor, suspending functions are supported.
 * Functions and properties annotated with [AutoWired] need to return one of the following types:
 * * [org.koin.core.module.Module]
 * * [com.gitlab.kordlib.kordx.commands.flow.ModuleModifier]
 * * [com.gitlab.kordlib.kordx.commands.processor.EventSource]
 * * [com.gitlab.kordlib.kordx.commands.processor.EventHandler]
 * * [com.gitlab.kordlib.kordx.commands.flow.EventFilter]
 * * [com.gitlab.kordlib.kordx.commands.flow.Precondition]
 * * [com.gitlab.kordlib.kordx.commands.model.CommandSet] (requires [ModuleName])
 * * [com.gitlab.kordlib.kordx.commands.processor.PrefixConfiguration]
 *
 * > The target can also be applied to files, in which case the annotation processor will act as if every
 * > function or property with the above mentioned return types would be annotated with [AutoWired]
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FILE)
annotation class AutoWired

/**
 * Annotation that specifies the module name for command sets.
 * This annotation is required for [AutoWired] [EventHandlers][com.gitlab.kordlib.kordx.commands.processor.EventHandler].
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class ModuleName(val name: String)

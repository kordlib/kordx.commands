package dev.kordx.commands.model.context

import dev.kordx.commands.model.command.CommandEvent
import dev.kordx.commands.model.processor.ProcessorContext

/**
 * A special context is compatible with all other contexts.
 *
 * [Commands][Command] with a CommonContext as their [context][Command.context] will be able to
 * receive events from every defined context.
 *
 * This context is effectively a [ProcessorContext] version of Kotlin's generic
 * [star projection](https://kotlinlang.org/docs/reference/generics.html#star-projections),
 * a command with this context will accept every other context purely because this context has
 * the lowest possible generic requirements.
 */
object CommonContext : ProcessorContext<Any?, Any?, CommandEvent>

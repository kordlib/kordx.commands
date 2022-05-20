package dev.kordx.commands.model.processor

import dev.kordx.commands.model.command.CommandEvent

/**
 * Context to be shared among command related items.
 * A ProcessorContext is a type token used in DSLs to provide the right generic types.
 *
 * @param S the [EventSource] context.
 * @param A the [dev.kordx.commands.argument.Argument] context.
 * @param C the [CommandEvent] context.
 */
interface ProcessorContext<in S, in A, in C: CommandEvent>

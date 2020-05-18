package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent

/**
 * Context to be shared among command related items.
 * A ProcessorContext is a type token used in DSLs to provide the right generic types.
 *
 * @param S the [EventSource] context.
 * @param A the [com.gitlab.kordlib.kordx.commands.argument.Argument] context.
 * @param C the [CommandEvent] context.
 */
interface ProcessorContext<in S, in A, in C: CommandEvent>

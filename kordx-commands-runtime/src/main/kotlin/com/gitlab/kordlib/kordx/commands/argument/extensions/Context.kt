package com.gitlab.kordlib.kordx.commands.argument.extensions

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

fun <T, CONTEXT, NEWCONTEXT : CONTEXT> Argument<T, CONTEXT>.withContext(newContext: ProcessorContext<*, NEWCONTEXT, *>): Argument<T, NEWCONTEXT> = this

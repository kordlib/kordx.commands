package com.gitlab.kordlib.kordx.commands.model.prefix

import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

class PrefixBuilder(val suppliers: MutableMap<ProcessorContext<*, *, *>, PrefixSupplier<*>> = mutableMapOf()) {

    fun <S, A, E : CommandEvent> add(context: ProcessorContext<S, A, E>, supplier: suspend (S) -> String) {
        suppliers[context] = supplier
    }

    fun build(): Prefix = Prefix(suppliers.toMap())

}

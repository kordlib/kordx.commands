package com.gitlab.kordlib.kordx.commands.model.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.model.precondition.Precondition
import com.gitlab.kordlib.kordx.commands.model.metadata.Metadata
import com.gitlab.kordlib.kordx.commands.model.module.Module
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext
import org.koin.core.Koin
import org.koin.core.KoinComponent

class Command<T: CommandContext>(
        val name: String,
        val moduleName: String,
        val context: ProcessorContext<*, *, T>,
        val metadata: Metadata,
        val arguments: List<Argument<*, *>>,
        val modules: Map<String, Module>,
        val preconditions: List<Precondition<T>>,
        private val koin: Koin,
        private val execution: suspend (T, List<*>) -> Unit
) : KoinComponent {
    override fun getKoin(): Koin = koin

    val module: Module get() = modules[moduleName] ?: error("expected module")

    suspend operator fun invoke(context: T, arguments: List<*>) = execution(context, arguments)
}

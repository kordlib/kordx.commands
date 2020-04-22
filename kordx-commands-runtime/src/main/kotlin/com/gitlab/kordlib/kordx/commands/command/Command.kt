package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import org.koin.core.Koin
import org.koin.core.KoinComponent

class Command<T: CommandContext>(
        val name: String,
        val moduleName: String,
        val context: PipeContext<*, *, *>,
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

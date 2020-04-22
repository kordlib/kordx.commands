package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import org.koin.core.Koin

@CommandsBuilder
class CommandBuilder<S, A, COMMANDCONTEXT : CommandContext>(
        val name: String,
        val moduleName: String,
        val context: PipeContext<S, A, COMMANDCONTEXT>,
        val metaData: MutableMetadata = MutableMetadata(),
        val preconditions: MutableList<Precondition<COMMANDCONTEXT>> = mutableListOf()
) {
    lateinit var execution: suspend (COMMANDCONTEXT, List<*>) -> Unit
    var arguments: List<Argument<*, A>> = emptyList()

    fun build(modules: Map<String, Module>, koin: Koin): Command<COMMANDCONTEXT> {
        return Command(name, moduleName, context, metaData, arguments, modules, preconditions, koin) { event, items ->
            execution.invoke(event, items)
        }
    }
}

fun <T : CommandContext> CommandBuilder<*, *, T>.precondition(
        priority: Long = 0,
        precondition: suspend T.() -> Boolean
) {
    preconditions += com.gitlab.kordlib.kordx.commands.flow.precondition(context, priority, precondition)
}


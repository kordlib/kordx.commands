package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder

@CommandsBuilder
class CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>(
        val name: String,
        val moduleName: String,
        val context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>,
        val metaData: MutableMetadata = MutableMetadata(),
        val preconditions: MutableList<Precondition<CONTEXT>> = mutableListOf()
) {
    lateinit var execution: suspend (CONTEXT, List<*>) -> Unit
    var arguments: List<Argument<*, ARGUMENTCONTEXT>> = emptyList()

    fun build(modules: Map<String, Module>): Command<CONTEXT> {
        return Command(name, moduleName, context, metaData, arguments, modules, preconditions) { event, items ->
            execution.invoke(event, items)
        }
    }
}

fun <SCONTEXT, ACOUNTEXT, ECONTEXT> CommandBuilder<SCONTEXT, ACOUNTEXT, ECONTEXT>.precondition(
        priority: Long = 0,
        precondition: suspend ECONTEXT.() -> Boolean
) {
    preconditions += com.gitlab.kordlib.kordx.commands.flow.precondition(context, priority, precondition)
}

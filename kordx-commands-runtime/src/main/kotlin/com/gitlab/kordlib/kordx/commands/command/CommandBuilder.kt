package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.primitives.IntArgument
import com.gitlab.kordlib.kordx.commands.flow.Precondition
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.internal.cast

@CommandsBuilder
data class CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT : EventContext>(
        val name: String,
        val moduleName: String,
        val context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>,
        val metaData: MutableMetaData = MutableMetaData(),
        val preconditions: MutableList<Precondition<CONTEXT>> = mutableListOf()
) {
    fun build(modules: Map<String, Module>): Command<CONTEXT> {
        return Command(name, moduleName, context, metaData, metaData[Arguments].orEmpty(), modules, preconditions) { event, items ->
            val function = metaData[Execution]!! as CommandExecution<Any>
            function.invoke(event, items)
        }
    }
}

fun <SCONTEXT, ACOUNTEXT, ECONTEXT : EventContext> CommandBuilder<SCONTEXT, ACOUNTEXT, ECONTEXT>.precondition(
        priority: Long = 0,
        precondition: suspend ECONTEXT.(PreconditionResult.Companion) -> PreconditionResult
) {
    preconditions += com.gitlab.kordlib.kordx.commands.flow.precondition(context, priority, precondition)
}

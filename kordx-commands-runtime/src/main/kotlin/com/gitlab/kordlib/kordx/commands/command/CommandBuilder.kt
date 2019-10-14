package com.gitlab.kordlib.kordx.commands.command

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.event.ArgumentCollection
import com.gitlab.kordlib.kordx.commands.event.args
import com.gitlab.kordlib.kordx.commands.internal.CommandsBuilder
import com.gitlab.kordlib.kordx.commands.internal.cast
import kotlinx.coroutines.flow.Flow

@CommandsBuilder
data class CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT : EventContext>(
        val name: String,
        val moduleName: String,
        val context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>,
        val metaData: MutableMetaData = MutableMetaData()
) {
    fun build(modules: Map<String, Module>): Command<CONTEXT> {
        return Command(name, moduleName, context, metaData, metaData[Arguments]!!, modules) { event, items ->
            val function = metaData[Execution]!! as CommandExecution<Any>
            function.invoke(event, items)
        }
    }
}

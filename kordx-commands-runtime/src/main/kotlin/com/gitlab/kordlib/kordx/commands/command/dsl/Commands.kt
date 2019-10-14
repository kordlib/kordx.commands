package com.gitlab.kordlib.kordx.commands.command.dsl

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.EventContext
import com.gitlab.kordlib.kordx.commands.command.ModuleBuilder

fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> commands(
        context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>,
        builder: ModuleBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>.() -> Unit
): CommandSet = object : CommandSet {
    override fun ModuleBuilder<*, *, *>.apply() {
        commands += ModuleBuilder(name, context, metaData, commands).apply(builder).commands
    }
}

interface CommandSet {
    fun ModuleBuilder<*, *, *>.apply()
}
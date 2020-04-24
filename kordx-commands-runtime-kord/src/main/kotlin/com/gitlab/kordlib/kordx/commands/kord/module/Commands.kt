package com.gitlab.kordlib.kordx.commands.kord.module

import com.gitlab.kordlib.kordx.commands.kord.model.command.KordCommandBuilder
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.model.module.CommandSet

fun commands(
        builder: KordModuleBuilder.() -> Unit
) = com.gitlab.kordlib.kordx.commands.model.module.commands(KordContext, builder)

fun command(
        name: String,
        builder: KordCommandBuilder.() -> Unit
): CommandSet = com.gitlab.kordlib.kordx.commands.model.module.command(KordContext, name, builder)

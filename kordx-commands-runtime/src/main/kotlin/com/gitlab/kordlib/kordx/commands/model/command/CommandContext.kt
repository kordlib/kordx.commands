package com.gitlab.kordlib.kordx.commands.model.command

import com.gitlab.kordlib.kordx.commands.model.module.Module
import com.gitlab.kordlib.kordx.commands.model.processor.CommandProcessor
import org.koin.core.KoinComponent

interface CommandContext : KoinComponent {
    val command: Command<*>
    val module: Module get() = command.module
    val commands: Map<String, Command<*>>
    val modules: Map<String, Module> get() = command.modules
    val processor: CommandProcessor
}
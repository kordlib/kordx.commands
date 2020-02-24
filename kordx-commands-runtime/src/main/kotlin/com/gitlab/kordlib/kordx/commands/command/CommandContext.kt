package com.gitlab.kordlib.kordx.commands.command

import org.koin.core.KoinComponent

interface CommandContext : KoinComponent {
    val command: Command<*>
    val module: Module get() = command.module
    val commands: Map<String, Command<*>>
    val modules: Map<String, Module> get() = command.modules
}
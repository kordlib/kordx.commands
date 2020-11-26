package com.gitlab.kordlib.kordx.commands.kord.model.command

import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandEvent
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter
import com.gitlab.kordlib.kordx.commands.model.command.CommandBuilder

typealias KordCommandBuilder = CommandBuilder<KordEventAdapter, KordEventAdapter, KordCommandEvent>

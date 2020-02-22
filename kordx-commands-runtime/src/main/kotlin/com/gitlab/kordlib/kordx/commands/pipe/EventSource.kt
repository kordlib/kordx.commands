package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import kotlinx.coroutines.flow.Flow

interface EventSource<SOURCECONTEXT> {
    val events: Flow<SOURCECONTEXT>
    val context: CommandContext<SOURCECONTEXT, *, *>
}
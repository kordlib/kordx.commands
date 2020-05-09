package com.gitlab.kordlib.kordx.commands.model.processor

import com.gitlab.kordlib.kordx.commands.model.command.CommandEvent

interface ProcessorContext<in S, in A, in C: CommandEvent>
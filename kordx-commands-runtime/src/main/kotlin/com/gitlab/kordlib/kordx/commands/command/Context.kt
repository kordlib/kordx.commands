package com.gitlab.kordlib.kordx.commands.command

interface PipeContext<in S, in A, in C: CommandContext>

object CommonContext : PipeContext<Any?, Any?, CommandContext>

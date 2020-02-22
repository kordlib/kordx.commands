package com.gitlab.kordlib.kordx.commands.command

interface CommandContext<in SOURCECONTEXT, in ARGUMENTCONTEXT, in EVENTCONTEXT>

object CommonContext : CommandContext<Any?, Any?, Any?>

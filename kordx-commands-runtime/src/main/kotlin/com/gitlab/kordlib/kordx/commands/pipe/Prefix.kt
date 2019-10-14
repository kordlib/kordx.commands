package com.gitlab.kordlib.kordx.commands.pipe

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.CommonContext
import com.gitlab.kordlib.kordx.commands.command.EventContext


interface Prefix<in SOURCECONTEXT, in ARGUMENTCONTEXT, in EVENTCONTEXT : EventContext> {
    val context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>

    suspend fun get(context: SOURCECONTEXT): String

    companion object {
        fun literal(prefix: String): Prefix<Any?, Any?, *> = object : Prefix<Any?, Any?, EventContext> {
            override val context: CommandContext<Any?, Any?, EventContext>
                get() = CommonContext

            override suspend fun get(context: Any?): String = prefix
        }

        fun <SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT : EventContext> literal(
                prefix: String,
                context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
        ): Prefix<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT> = object : Prefix<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT> {
            override val context: CommandContext<SOURCECONTEXT, ARGUMENTCONTEXT, EVENTCONTEXT>
                get() = context

            override suspend fun get(context: SOURCECONTEXT): String = prefix
        }
    }
}

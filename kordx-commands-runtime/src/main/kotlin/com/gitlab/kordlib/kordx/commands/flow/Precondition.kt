package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.command.CommandContext
import com.gitlab.kordlib.kordx.commands.command.EventContext

interface Precondition<EVENTCONTEXT : EventContext> {
    val priority: Long get() = 0
    val context: CommandContext<*, *, EVENTCONTEXT>
    suspend operator fun invoke(event: EVENTCONTEXT): PreconditionResult
}

fun <EVENTCONTEXT : EventContext> precondition(
        context: CommandContext<*, *, EVENTCONTEXT>,
        priority: Long = 0,
        precondition: suspend EVENTCONTEXT.(PreconditionResult.Companion) -> PreconditionResult
) = object : Precondition<EVENTCONTEXT> {
    override val priority: Long get() = priority
    override val context: CommandContext<*, *, EVENTCONTEXT> = context
    override suspend fun invoke(event: EVENTCONTEXT): PreconditionResult = precondition(event, PreconditionResult)
}

sealed class PreconditionResult {
    class Fail(val message: String) : PreconditionResult()
    object Pass : PreconditionResult()

    companion object {
        fun fail(message: String) = PreconditionResult.Fail(message)
        fun pass() = PreconditionResult.Pass
    }
}
package com.gitlab.kordlib.kordx.commands.flow

import com.gitlab.kordlib.kordx.commands.event.CommandEvent

typealias Precondition = (event: CommandEvent<*>) -> PreconditionResult

fun precondition(precondition: Precondition) = precondition

sealed class PreconditionResult {
    class Fail(val message: String) : PreconditionResult()
    object Success : PreconditionResult()
}
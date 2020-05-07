package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.switchOnFail
import com.gitlab.kordlib.kordx.commands.kord.model.KordEvent

typealias ArgumentDelegate<T> = suspend KordEvent.() -> ArgumentResult<T>

fun <T> argumentDelegate(delegate: suspend KordEvent.() -> ArgumentResult<T>): ArgumentDelegate<T> = delegate

infix fun <T> Argument<T, MessageCreateEvent>.or(
        delegate: ArgumentDelegate<T>
): Argument<T, MessageCreateEvent> = DelegateArgument(this, delegate)

private class DelegateArgument<T>(
        private val argument: Argument<T, MessageCreateEvent>,
        private val delegate: ArgumentDelegate<T>
) : Argument<T, MessageCreateEvent> by argument {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: MessageCreateEvent): ArgumentResult<T> {
       return argument.parse(words, fromIndex, context).switchOnFail { delegate(KordEvent(context)) }
    }

}

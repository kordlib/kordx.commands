package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.switchOnFail
import com.gitlab.kordlib.kordx.commands.kord.context.KordEvent

typealias ArgumentDelegate<T> = suspend KordEvent.() -> Result<T>

fun <T> argumentDelegate(delegate: suspend KordEvent.() -> Result<T>): ArgumentDelegate<T> = delegate

infix fun <T> Argument<T, MessageCreateEvent>.or(
        delegate: ArgumentDelegate<T>
): Argument<T, MessageCreateEvent> = DelegateArgument(this, delegate)

class DelegateArgument<T>(
        private val argument: Argument<T, MessageCreateEvent>,
        private val delegate: ArgumentDelegate<T>
) : Argument<T, MessageCreateEvent> by argument {

    override suspend fun parse(words: List<String>, fromIndex: Int, context: MessageCreateEvent): Result<T> {
        return argument.parse(words, fromIndex, context).switchOnFail { delegate(KordEvent(context)) }
    }

}

package dev.kordx.commands.kord.argument

import dev.kord.core.event.message.MessageCreateEvent
import dev.kordx.commands.argument.Argument
import dev.kordx.commands.argument.result.ArgumentResult
import dev.kordx.commands.argument.result.extension.switchOnFail
import dev.kordx.commands.kord.model.KordEvent

typealias ArgumentDelegate<T> = suspend KordEvent.() -> ArgumentResult<T>

/**
 * Returns the [delegate] as a [ArgumentDelegate].
 */
fun <T> argumentDelegate(delegate: suspend KordEvent.() -> ArgumentResult<T>): ArgumentDelegate<T> = delegate

/**
 * Returns an Argument that delegates to the [delegate] if this argument returns a [ArgumentResult.Failure].
 */
infix fun <T> Argument<T, MessageCreateEvent>.or(
        delegate: ArgumentDelegate<T>
): Argument<T, MessageCreateEvent> = DelegateArgument(this, delegate)

private class DelegateArgument<T>(
    private val argument: Argument<T, MessageCreateEvent>,
    private val delegate: ArgumentDelegate<T>
) : Argument<T, MessageCreateEvent> by argument {

    override suspend fun parse(text: String, fromIndex: Int, context: MessageCreateEvent): ArgumentResult<T> {
       return argument.parse(text, fromIndex, context).switchOnFail { delegate(KordEvent(context)) }
    }

}

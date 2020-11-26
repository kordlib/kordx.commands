package com.gitlab.kordlib.kordx.commands.kord.argument

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.argument.result.extension.switchOnFail
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordEventAdapter

typealias ArgumentDelegate<T> = suspend KordEventAdapter.() -> ArgumentResult<T>

/**
 * Returns the [delegate] as a [ArgumentDelegate].
 */
fun <T> argumentDelegate(delegate: suspend KordEventAdapter.() -> ArgumentResult<T>): ArgumentDelegate<T> = delegate

/**
 * Returns an Argument that delegates to the [delegate] if this argument returns a [ArgumentResult.Failure].
 */
infix fun <T> Argument<T, KordEventAdapter>.or(
        delegate: ArgumentDelegate<T>
): Argument<T, KordEventAdapter> = DelegateArgument(this, delegate)

private class DelegateArgument<T>(
        private val argument: Argument<T, KordEventAdapter>,
        private val delegate: ArgumentDelegate<T>
) : Argument<T, KordEventAdapter> by argument {

    override suspend fun parse(text: String, fromIndex: Int, context: KordEventAdapter): ArgumentResult<T> {
        return argument.parse(text, fromIndex, context).switchOnFail { delegate(context) }
    }

}

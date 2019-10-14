package com.gitlab.kordlib.kordx.commands.temp.adapters

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.discord.MemberArgument
import com.gitlab.kordlib.kordx.commands.argument.text.TextArgument
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.command.dsl.module
import com.gitlab.kordlib.kordx.commands.event.ArgumentCollection
import com.gitlab.kordlib.kordx.commands.event.args
import com.gitlab.kordlib.kordx.commands.flow.ModuleGenerator
import com.gitlab.kordlib.kordx.commands.flow.eventFilter
import com.gitlab.kordlib.kordx.commands.internal.cast
import com.gitlab.kordlib.kordx.commands.pipe.PipeConfig
import me.aberrantfox.kjdautils.api.startBot
import me.aberrantfox.kjdautils.internal.arguments.IntegerArg

suspend fun main(args: Array<String>) {
    val kord = Kord(args.first())
    val kutils = startBot(args.first())
    PipeConfig {
        moduleGenerators += ModuleGenerator.from(myModule())
        prefixes += Prefix.literal("!")

        eventFilters += notBot

        this.eventSources += KordEventSource(kord)
        this.eventSources += KUtilsEventSource(kutils)
        this.eventSources += CLIEventSource
    }

    kord.login()
}

val notBot = eventFilter(KordContext) { message.author?.isBot == true }

fun myModule() = module("commands", CommonContext) {

    command("ping") {
        execute {
            respond("pong")
        }
    }

    command("echo") {
        execute(TextArgument) { text ->
            respond(text)
        }
    }

    command("tell", KordContext) {
        execute(MemberArgument, TextArgument) { member, message ->
            respond("${member.mention} $message")
        }
    }

    command("add", KUtilsContext) {
        execute(IntegerArg, IntegerArg) {
            val (first, second) = it.args
            it.respond("${first + second}")
        }
    }

}

//commands dsl
fun <SOURCECONTEXT, ARGUMENTCONTEXT, T, CONTEXT : EventContext> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>.execute(
        collection: ArgumentCollection<T, ARGUMENTCONTEXT>, execution: suspend CONTEXT.(T) -> Unit
) {
    metaData[Arguments] = collection.arguments
    metaData[Execution] = { context, arguments ->
        execution.invoke(context.cast(), collection.bundle(arguments))
    }
}

fun <SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT : EventContext> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>.execute(
        execution: suspend CONTEXT.() -> Unit
) {
    metaData[Arguments] = emptyList()
    metaData[Execution] = { context, _ ->
        execution.invoke(context.cast())
    }
}

fun <SOURCECONTEXT, ARGUMENTCONTEXT, T, CONTEXT : EventContext> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>.execute(
        argument: Argument<T, ARGUMENTCONTEXT>,
        execution: suspend CONTEXT.(T) -> Unit
) {
    execute(args(argument)) {
        execution(it.first)
    }
}

fun <SOURCECONTEXT, ARGUMENTCONTEXT, A, B, CONTEXT : EventContext> CommandBuilder<SOURCECONTEXT, ARGUMENTCONTEXT, CONTEXT>.execute(
        first: Argument<A, ARGUMENTCONTEXT>,
        second: Argument<B, ARGUMENTCONTEXT>,
        execution: suspend CONTEXT.(A, B) -> Unit
) {
    execute(args(first, second)) {
        execution(it.first, it.second)
    }
}
package com.gitlab.kordlib.kordx.commands.temp.adapters

import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.Result
import com.gitlab.kordlib.kordx.commands.argument.VariableLengthArgument
import com.gitlab.kordlib.kordx.commands.command.*
import com.gitlab.kordlib.kordx.commands.command.Command
import com.gitlab.kordlib.kordx.commands.event.args
import com.gitlab.kordlib.kordx.commands.flow.PreconditionResult
import com.google.common.eventbus.Subscribe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.aberrantfox.kjdautils.api.KUtils
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.CommandStruct
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.aberrantfox.kjdautils.internal.event.EventRegister
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

object KUtilsContext : CommandContext<GuildMessageReceivedEvent, CommandEvent<*>, KUtilsEventAdapter>
class KUtilsEventAdapter(val event: CommandEvent<*>, override val command: Command<*>, override val module: Module?) : EventContext {

    override suspend fun respond(text: String): Any? {
        return event.respond(text)
    }
}

class KUtilsContextConverter(val kutils: KUtils) : ContextConverter<GuildMessageReceivedEvent, CommandEvent<*>, KUtilsEventAdapter> {
    override fun supports(context: CommandContext<*, *, *>): Boolean = context is KUtilsContext //|| context is CommonContext
    override suspend fun convert(context: GuildMessageReceivedEvent): CommandEvent<*> {
        return CommandEvent<ArgumentContainer>(CommandStruct("", listOf(), false), CommandsContainer(HashMap()), DiscordContext(false, kutils.discord, context.message))
    }

    override suspend fun CommandEvent<*>.notFound(command: String) {
        respond("$command not found")
    }

    override suspend fun CommandEvent<*>.rejectArgument(command: Command<*>, words: List<String>, failure: Result.Failure<*>) {
        respond(failure.reason)
    }

    override suspend fun convert(context: CommandEvent<*>, command: Command<EventContext>, module: Module?, arguments: List<Argument<*, CommandEvent<*>>>): KUtilsEventAdapter {
        return KUtilsEventAdapter(context, command, module)
    }

    override suspend fun CommandEvent<*>.emptyInvocation() {
        respond("empty command invocation")
    }

    override suspend fun KUtilsEventAdapter.rejectPrecondition(command: Command<*>, failure: PreconditionResult.Fail) {
        respond(failure.message)
    }

    override suspend fun toText(context: CommandEvent<*>): String = context.message.contentRaw
}

fun <A, B> CommandBuilder<GuildMessageReceivedEvent, CommandEvent<*>, KUtilsEventAdapter>.execute(first: ArgumentType<A>, second: ArgumentType<B>, execute: (CommandEvent<DoubleArg<A, B>>) -> Unit) {
    execute(args(first.toKord(), second.toKord())) {
        val event = this.event as CommandEvent<DoubleArg<A, B>>
        event.args = DoubleArg(it.first, it.second)
        execute(event)
    }
}

fun <A> ArgumentType<A>.toKord() = KUtilsArgumentAdapter(this)

class KUtilsEventSource(val kutils: KUtils) : EventSource<GuildMessageReceivedEvent> {
    private val channel = Channel<GuildMessageReceivedEvent>()

    init {
        EventRegister.eventBus.register(this)
    }

    @Subscribe
    fun onEvent(event: GuildMessageReceivedEvent) {
        GlobalScope.launch(Dispatchers.IO) {
            channel.send(event)
        }
    }

    override val context: CommandContext<GuildMessageReceivedEvent, *, *>
        get() = KUtilsContext
    override val converter: ContextConverter<GuildMessageReceivedEvent, *, *>
        get() = KUtilsContextConverter(kutils)

    override val events: Flow<GuildMessageReceivedEvent> = flow {
        for (guildMessageReceivedEvent in channel) {
            emit(guildMessageReceivedEvent)
        }
    }
}

class KUtilsArgumentAdapter<A>(val argument: ArgumentType<A>) : VariableLengthArgument<A, CommandEvent<*>>() {
    override val example: String
        get() = argument.examples.random()

    override val name: String
        get() = argument.name

    override suspend fun parse(words: List<String>, context: CommandEvent<*>): Result<A> {
        return when (val result = argument.convert(words.first(), words, context)) {
            is ArgumentResult.Success -> Result.Success(result.result, argument.actualSize(result))
            is ArgumentResult.Error -> Result.Failure(result.error, 0)
        }
    }
}

fun ArgumentType<*>.actualSize(result: ArgumentResult.Success<*>): Int = when(this.consumptionType) {
    ConsumptionType.Single -> 1
    ConsumptionType.Multiple -> result.consumed.size
    ConsumptionType.All -> Int.MAX_VALUE
}

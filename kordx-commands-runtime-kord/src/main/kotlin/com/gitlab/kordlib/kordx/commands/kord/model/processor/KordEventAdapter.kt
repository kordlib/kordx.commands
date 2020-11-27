package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.common.annotation.KordExperimental
import com.gitlab.kordlib.common.annotation.KordUnsafe
import com.gitlab.kordlib.common.entity.optional.Optional
import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.core.KordObject
import com.gitlab.kordlib.core.behavior.GuildBehavior
import com.gitlab.kordlib.core.behavior.MessageBehavior
import com.gitlab.kordlib.core.behavior.UserBehavior
import com.gitlab.kordlib.core.behavior.channel.MessageChannelBehavior
import com.gitlab.kordlib.core.entity.Member
import com.gitlab.kordlib.core.entity.Message
import com.gitlab.kordlib.core.event.Event
import com.gitlab.kordlib.core.event.message.MessageCreateEvent
import com.gitlab.kordlib.core.event.message.MessageUpdateEvent
import com.gitlab.kordlib.kordx.commands.argument.Argument
import com.gitlab.kordlib.kordx.commands.argument.result.ArgumentResult
import com.gitlab.kordlib.kordx.commands.kord.model.KordEvent
import com.gitlab.kordlib.rest.builder.message.MessageCreateBuilder
import kotlinx.coroutines.flow.*

/**
 * Event Adapter for the kord events.
 *
 * @param event Event to be wrapped.
 */
class KordEventAdapter internal constructor(val event: Event) : KordObject {

    override val kord: Kord
        get() = event.kord

    /**
     * The message behavior this event belongs to.
     */
    @OptIn(KordUnsafe::class, KordExperimental::class)
    val message: MessageBehavior
        get() = when (event) {
            is MessageCreateEvent -> event.message
            is MessageUpdateEvent -> kord.unsafe.message(event.channelId, event.messageId)
            else -> raiseWrongEventError(event)
        }

    /**
     * The content of this message.
     */
    val content: String
        get() = when (event) {
            is MessageCreateEvent -> event.message.content
            is MessageUpdateEvent -> event.new.content.value
                    ?: raiseWrongEventError(event)  // pre-filtered from [filterAndMapToAdapter]
            else -> raiseWrongEventError(event)
        }

    /**
     * The author in form of [UserBehavior] of this message, if it was created by a user.
     */
    @OptIn(KordUnsafe::class, KordExperimental::class)
    val author: UserBehavior?
        get() = when (event) {
            is MessageCreateEvent -> event.message.author
            is MessageUpdateEvent -> event.old?.author ?: event.new.author.value?.let { kord.unsafe.user(it.id) }
            else -> raiseWrongEventError(event)
        }

    /**
     * The channel behavior that this event belongs to.
     */
    val channel: MessageChannelBehavior
        get() = when (event) {
            is MessageCreateEvent -> event.message.channel
            is MessageUpdateEvent -> event.channel
            else -> raiseWrongEventError(event)
        }

    /**
     * The guild behavior this event belongs to, if message was created in a guild.
     */
    @OptIn(KordUnsafe::class, KordExperimental::class)
    val guild: GuildBehavior?
        get() = when (event) {
            is MessageCreateEvent -> event.guildId?.let { kord.unsafe.guild(it) }
            is MessageUpdateEvent -> event.old?.data?.guildId?.value?.let { kord.unsafe.guild(it) }
                    ?: event.new.guildId.value?.let { kord.unsafe.guild(it) }
            else -> raiseWrongEventError(event)
        }

    /**
     * Resolves the [author] as a [Member] if the [event] was spawned in a Guild.
     */
    suspend fun resolveAuthorAsMember(): Member? {
        val guildId = when (event) {
            is MessageCreateEvent -> event.guildId
            is MessageUpdateEvent -> event.old?.data?.guildId?.value ?: event.new.guildId.value
            else -> raiseWrongEventError(event)
        } ?: return null

        return author?.asMemberOrNull(guildId)
    }

    /**
     * Creates a message in the [KordEvent.channel].
     *
     * @param message The [MessageCreateBuilder.content] of the message.
     */
    suspend fun respond(message: String): Message {
        return channel.createMessage(message)
    }

    /**
     *  Suspends until the user invoking this command enters a message in
     *  the [KordEventAdapter.channel] that is accepted by the given [argument].
     *
     *  If last message was unaccepted, it can be edited.
     *
     *  ```kotlin
     * command("ban")
     *     invoke {
     *         respond("Specify the user to ban")
     *         val member = read(MemberArgument, { it.message.content == "cancel" }) {
     *             if (it.id != kord.selfId) return@read true
     *
     *             respond("Can't ban myself.")
     *             false
     *         }
     *
     *         member.ban()
     *     }
     * }
     * ```
     * > This function doesn't return until a valid value is entered, which might negatively impact user experience.
     * > If this is a concern, consider using the method overload that accepts an escape filter.
     *
     *  @param filter an additional filter for generated values, ignoring all values that return false.
     *  @return an item [T] generated by the [argument]
     */
    suspend fun <T> read(
            argument: Argument<T, KordEventAdapter>,
            filter: suspend (T) -> Boolean = { true }
    ): T = kord.events
            .filterAndMapToAdapter()
            .filter { it.author?.id == author!!.id }
            .filter { it.channel.id == channel.id }
            .rememberLastMessageCreateAndZip()
            .filter { it.second.event is MessageCreateEvent || it.first?.message?.id == it.second.message.id }
            .map { it.second }
            .map { argument.parse(it.content, 0, it) }
            .onEach { if (it is ArgumentResult.Failure) respond(it.reason) }
            .filterIsInstance<ArgumentResult.Success<T>>()
            .map { it.item }
            .filter(filter)
            .take(1)
            .single()

    /**
     *  Suspends until the user invoking this command enters a message in
     *  the [KordEventAdapter.channel] that is accepted by the given [argument].
     *
     *  If last message was unaccepted, it can be edited.
     *
     *  ```kotlin
     *  command("ban")
     *     invoke {
     *         respond("Specify the user to ban")
     *         val member = read(MemberArgument, { it.message.content == "cancel" }) {
     *             if (it.id != kord.selfId) return@read true
     *
     *             respond("Can't ban myself.")
     *             false
     *         }
     *
     *         member?.ban()
     *     }
     * }
     * ```
     *
     *  @param filter an additional filter for generated values, ignoring all values that return false.
     *  @param escape a filter that stops this function from taking input when returning true,
     *  making this function return `null`.
     *  @return an item [T] generated by the [argument]
     */
    suspend fun <T : Any> read(
            argument: Argument<T, KordEventAdapter>,
            escape: suspend (KordEventAdapter) -> Boolean,
            filter: suspend (T) -> Boolean = { true }
    ): T? = kord.events
            .filterAndMapToAdapter()
            .filter { it.author?.id == author!!.id }
            .filter { it.channel.id == channel.id }
            .takeWhile { !escape(it) }
            .rememberLastMessageCreateAndZip()
            .filter { it.second.event is MessageCreateEvent || it.first?.message?.id == it.second.message.id }
            .map { it.second }
            .map { argument.parse(it.content, 0, it) }
            .onEach { if (it is ArgumentResult.Failure) respond(it.reason) }
            .filterIsInstance<ArgumentResult.Success<T>>()
            .map { it.item }
            .filter(filter)
            .take(1)
            .singleOrNull()

    internal fun replaceContent(content: String): KordEventAdapter {
        val updatedEvent = when (event) {
            is MessageCreateEvent -> {
                val data = event.message.data.copy(content = content)
                with(event) {
                    MessageCreateEvent(Message(data, kord), guildId, member, shard, supplier)
                }
            }
            is MessageUpdateEvent -> {
                val newPartialMessage = event.new.copy(content = Optional(content))
                with(event) {
                    MessageUpdateEvent(messageId, channelId, newPartialMessage, old, kord, shard)
                }
            }
            else -> raiseWrongEventError(event)
        }

        return KordEventAdapter(updatedEvent)
    }

    companion object {
        @Suppress("MaxLineLength")
        private fun raiseWrongEventError(event: Event): Nothing =
                throw IllegalArgumentException("Wrong event ${event::class.simpleName} is wrapped around KordEventAdapter, only MessageCreateEvent and MessageUpdateEvent (with message content changed) should be wrapped.")

        private fun
                Flow<KordEventAdapter>.rememberLastMessageCreateAndZip():
                Flow<Pair<KordEventAdapter?, KordEventAdapter>> = flow {
            var lastMessageCreate: KordEventAdapter? = null

            collect {
                emit(lastMessageCreate to it)

                if (it.event is MessageCreateEvent) {
                    lastMessageCreate = it
                }
            }
        }
    }
}

/**
 * Filters the [Flow] of Kord [Event]s, and maps them to [KordEventAdapter].
 */
internal fun Flow<Event>.filterAndMapToAdapter(): Flow<KordEventAdapter> = transform {
    if (it is MessageCreateEvent || it is MessageUpdateEvent && it.new.content is Optional.Value) {
        return@transform emit(KordEventAdapter(it))
    }
}

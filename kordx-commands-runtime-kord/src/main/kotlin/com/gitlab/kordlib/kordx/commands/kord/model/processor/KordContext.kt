package com.gitlab.kordlib.kordx.commands.kord.model.processor

import com.gitlab.kordlib.kordx.commands.kord.model.context.KordCommandEvent
import com.gitlab.kordlib.kordx.commands.model.processor.ProcessorContext

/**
 * Type token for all Kord command entities.
 */
interface KordContext : ProcessorContext<KordEventAdapter, KordEventAdapter, KordCommandEvent> {
    companion object : KordContext
}

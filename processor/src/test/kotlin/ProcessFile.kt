@file:dev.kord.x.commands.annotation.AutoWired
@file:Suppress("UNUSED_PARAMETER", "unused", "RedundantSuspendModifier")

import dev.kord.x.commands.kord.bot
import dev.kord.x.commands.kord.model.prefix.kord
import dev.kord.x.commands.kord.model.processor.KordContext
import dev.kord.x.commands.kord.model.processor.KordContextConverter
import dev.kord.x.commands.kord.model.processor.KordErrorHandler
import dev.kord.x.commands.kord.module.command
import dev.kord.x.commands.kord.module.commands
import dev.kord.x.commands.kord.module.module
import dev.kord.x.commands.model.context.CommonContext
import dev.kord.x.commands.model.prefix.literal
import dev.kord.x.commands.model.prefix.prefix
import dev.kord.x.commands.model.processor.BaseEventHandler
import dev.kord.x.commands.model.processor.EventSource
import kapt.kotlin.generated.configure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.dsl.module

fun exampleSource() = object : EventSource<Unit> {
    override val context = CommonContext

    override val events: Flow<Unit>
        get() = emptyFlow()
}

fun exampleModule() = module("test-module") {}

suspend fun suspendingModule() = module("test-module2") {}

fun dependencies() = module {}

@dev.kord.x.commands.annotation.ModuleName("test-module")
fun extraCommands() = commands {}

@dev.kord.x.commands.annotation.ModuleName("test-module")
fun extraCommand(dependency: String) = command("pang") {}

fun nothingToDoWithAutoWired() = Unit

val myHandler = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())

@get:dev.kord.x.commands.annotation.ModuleName("test-module")
val propertyCommand
    get() = command("swing") {}

val kordPrefix = prefix {
    kord { literal("+") }
}

suspend fun testReference() = bot("sample") {
    configure()
}


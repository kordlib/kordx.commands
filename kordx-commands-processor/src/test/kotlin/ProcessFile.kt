@file:AutoWired
@file:Suppress("UNUSED_PARAMETER", "unused", "RedundantSuspendModifier")

import dev.kordx.commands.annotation.AutoWired
import dev.kordx.commands.kord.bot
import dev.kordx.commands.kord.model.prefix.kord
import dev.kordx.commands.kord.model.processor.KordContext
import dev.kordx.commands.kord.model.processor.KordContextConverter
import dev.kordx.commands.kord.model.processor.KordErrorHandler
import dev.kordx.commands.kord.module.command
import dev.kordx.commands.kord.module.commands
import dev.kordx.commands.kord.module.module
import dev.kordx.commands.model.context.CommonContext
import dev.kordx.commands.model.prefix.literal
import dev.kordx.commands.model.prefix.prefix
import dev.kordx.commands.model.processor.BaseEventHandler
import dev.kordx.commands.model.processor.EventSource
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

@dev.kordx.commands.annotation.ModuleName("test-module")
fun extraCommands() = commands {}

@dev.kordx.commands.annotation.ModuleName("test-module")
fun extraCommand(dependency: String) = command("pang") {}

fun nothingToDoWithAutoWired() {}

val myHandler = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())

@get:dev.kordx.commands.annotation.ModuleName("test-module")
val propertyCommand
    get() = command("swing") {}

val kordPrefix = prefix {
    kord { literal("+") }
}

suspend fun testReference() = bot("sample") {
    configure()
}


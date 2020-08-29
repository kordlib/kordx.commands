@file:AutoWired
@file:Suppress("UNUSED_PARAMETER", "unused", "RedundantSuspendModifier")

import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.kord.bot
import com.gitlab.kordlib.kordx.commands.kord.model.prefix.kord
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContext
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordContextConverter
import com.gitlab.kordlib.kordx.commands.kord.model.processor.KordErrorHandler
import com.gitlab.kordlib.kordx.commands.kord.module.command
import com.gitlab.kordlib.kordx.commands.kord.module.commands
import com.gitlab.kordlib.kordx.commands.kord.module.module
import com.gitlab.kordlib.kordx.commands.model.context.CommonContext
import com.gitlab.kordlib.kordx.commands.model.prefix.literal
import com.gitlab.kordlib.kordx.commands.model.prefix.prefix
import com.gitlab.kordlib.kordx.commands.model.processor.BaseEventHandler
import com.gitlab.kordlib.kordx.commands.model.processor.EventSource
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

@ModuleName("test-module")
fun extraCommands() = commands {}

@ModuleName("test-module")
fun extraCommand(dependency: String) = command("pang") {}

fun nothingToDoWithAutoWired() {}

val myHandler = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())

@get:ModuleName("test-module")
val propertyCommand
    get() = command("swing") {}

val kordPrefix = prefix {
    kord { literal("+") }
}

suspend fun testReference() = bot("sample") {
    configure()
}


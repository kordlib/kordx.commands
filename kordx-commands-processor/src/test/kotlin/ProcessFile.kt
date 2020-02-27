@file:AutoWired

import com.gitlab.kordlib.core.Kord
import com.gitlab.kordlib.kordx.commands.annotation.AutoWired
import com.gitlab.kordlib.kordx.commands.annotation.ModuleName
import com.gitlab.kordlib.kordx.commands.command.CommonContext
import com.gitlab.kordlib.kordx.commands.command.invoke
import com.gitlab.kordlib.kordx.commands.kord.*
import com.gitlab.kordlib.kordx.commands.kord.context.KordContext
import com.gitlab.kordlib.kordx.commands.kord.context.KordContextConverter
import com.gitlab.kordlib.kordx.commands.kord.context.KordErrorHandler
import com.gitlab.kordlib.kordx.commands.pipe.BaseEventHandler
import com.gitlab.kordlib.kordx.commands.pipe.EventSource
import com.gitlab.kordlib.kordx.commands.pipe.prefix
import kapt.kotlin.generated.configure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.dsl.module

fun exampleSource() = object : EventSource<Unit> {
    override val context = CommonContext

    override val events: Flow<Unit>
        get() = emptyFlow()
}

fun exampleModule() = module<Kord>("test-module") {}

fun dependencies() = module {}

@ModuleName("test-module")
fun extraCommands() = commands<Kord> {}

@ModuleName("test-module")
fun extraCommand(dependency: String) = command<Kord>("pang") {}

fun nothingToDoWithAutoWired() {}

val myHandler = BaseEventHandler(KordContext, KordContextConverter, KordErrorHandler())

@get:ModuleName("test-module")
val propertyCommand
    get() = command<Kord>("swing") {}

val kordPrefix get() = prefix {
    add<Kord> { "+" }
}

suspend fun testReference() = bot("sample"){
    configure()
}


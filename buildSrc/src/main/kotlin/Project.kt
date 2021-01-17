import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

val DependencyHandlerScope.processor get() = project(":kordx-commands-processor")
val DependencyHandlerScope.runtime get() = project(":kordx-commands-runtime")
val DependencyHandlerScope.`runtime-kord` get() = project(":kordx-commands-runtime-kord")

object Library {
    const val group = "dev.kord.x"
    val version = System.getenv("RELEASE_TAG") ?: System.getenv("GITHUB_SHA") ?: "undefined"
    const val description = "Kotlin Command library for Kord and other APIs"
}

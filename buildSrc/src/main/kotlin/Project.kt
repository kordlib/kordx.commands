import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

val DependencyHandlerScope.processor get() = project(":processor")
val DependencyHandlerScope.runtime get() = project(":runtime")
val DependencyHandlerScope.`runtime-kord` get() = project(":runtime-kord")


/**
 * whether the process has been invoked by JitPack
 */
val isJitPack get() = "true" == System.getenv("JITPACK")



object Library {
    private const val releaseVersion = "0.4.0-SNAPSHOT"
    val isSnapshot: Boolean get() = releaseVersion.endsWith("-SNAPSHOT")
    val isRelease: Boolean get() = !isSnapshot
    const val name = "commands"
    const val group = "dev.kord.x"
    val version: String = if (isJitPack) System.getenv("RELEASE_TAG")
    else releaseVersion
    const val url = "https://github.com/kordlib/kordx.commands"

    const val description = "Kotlin Command library for Kord and other APIs"
    const val projectUrl = "https://github.com/kordlib/kordx.commands"

    val isStableApi: Boolean get() = !isSnapshot
}

object Repo {
    const val releasesUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
    const val snapshotsUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
}

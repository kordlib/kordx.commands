val isJitPack get() = "true" == System.getenv("JITPACK")

object Library {
    const val name = "kordx.commands"
    const val group = "dev.kordx"
    val version: String
        get() = if (isJitPack) System.getenv("RELEASE_TAG")
        else {
            val tag = System.getenv("GITHUB_TAG_NAME")
            val branch = System.getenv("GITHUB_BRANCH_NAME")
            when {
                !tag.isNullOrBlank() -> tag
                !branch.isNullOrBlank() && branch.startsWith("refs/heads/") ->
                    branch.substringAfter("refs/heads/").replace("/", "-") + "-SNAPSHOT"
                else -> "undefined"
            }
        }

    val commitHash get() = System.getenv("GITHUB_SHA") ?: "unknown"

    // This environment variable isn't available out of the box, we set it ourselves
    val shortCommitHash get() = System.getenv("SHORT_SHA") ?: "unknown"

    const val description = "Kotlin Command library for Kord and other APIs"
    const val projectUrl = "https://github.com/kordlib/kordx.commands"

    val isSnapshot: Boolean get() = version.endsWith("-SNAPSHOT")

    /**
     * Whether the current API is considered stable, and should be compared to the 'golden' API dump.
     */
    val isRelease: Boolean get() = !isSnapshot && !isUndefined

    val isUndefined get() = version == "undefined"
}


object Repo {
    const val releasesUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    const val snapshotsUrl = "https://oss.sonatype.org/content/repositories/snapshots"
}
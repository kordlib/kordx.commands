import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Date

plugins {
    kotlin("jvm") version Versions.kotlin
    id("io.gitlab.arturbosch.detekt") version "1.9.0" apply false
    id("com.jfrog.bintray") version "1.8.1" apply false
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    kotlin("plugin.serialization") version "1.4.21" apply false
}

repositories {
    jcenter()
}

group = Library.group
version = Library.version

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    val kotlinComponent = components["kotlin"]

    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kordlib/Kord")
    }

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config = rootProject.files("config/detekt/detekt.yml")
    }

    val api by configurations.getting
    val implementation by configurations.getting
    val testImplementation by configurations.getting
    val testRuntimeOnly by configurations.getting

    dependencies {
        api(Dependencies.jdk8)
        api(Dependencies.`kotlinx-coroutines`)
        api(Dependencies.`kotlin-logging`)
        implementation(Dependencies.kotlinPoet)

        testImplementation(Dependencies.mockkDsl)
        testImplementation(Dependencies.mockk)
        testImplementation(Dependencies.`kotlin-test`)
        testImplementation(Dependencies.junit5)
        testImplementation(Dependencies.`kotlinx-coroutines-test`)
        testRuntimeOnly(Dependencies.`kotlin-reflect`)
        testImplementation(Dependencies.`junit-jupiter-api`)
        testImplementation(Dependencies.`junit-jupiter-params`)
        testRuntimeOnly(Dependencies.`junit-jupiter-engine`)
        testRuntimeOnly(Dependencies.sl4j)
    }

    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = Jvm.target
                freeCompilerArgs = freeCompilerArgs + arrayOf(
                    CompilerArguments.coroutines,
                    CompilerArguments.experimental,
                    CompilerArguments.inlineClasses
                )
            }
        }

        withType<Test> {
            useJUnitPlatform {
                includeEngines("junit-jupiter")
            }
        }

        val sourcesJar by registering(Jar::class) {
            archiveClassifier.set("sources")
            from(sourceSets.main.get().allSource)
        }

        configure<PublishingExtension> {
            publications {
                register<MavenPublication>("Commands") {
                    from(kotlinComponent)
                    groupId = Library.group
                    artifactId = project.name
                    version = Library.version

                    artifact(sourcesJar)
                }
            }
        }
    }

    configure<BintrayExtension> {
            user = System.getenv("BINTRAY_USER")
            key = System.getenv("BINTRAY_KEY")
            setPublications("Commands")
            publish = true
            pkg {
                repo = "Kord"
                name = "kordx.commands"
                userOrg = "kordlib"
                setLicenses("MIT")
                vcsUrl = "https://gitlab.com/kordlib/kordx.commands.git"
                websiteUrl = "https://gitlab.com/kordlib/kordx.commands"
                issueTrackerUrl = "https://gitlab.com/kordlib/kordx.commands/issues"

                version {
                    name = Library.version
                    desc = "Command library for Kotlin"
                    released = Date().toString()
                    vcsTag = Library.version
                }
            }
    }
}

fun BintrayExtension.pkg(block: BintrayExtension.PackageConfig.() -> Unit) =
    pkg(delegateClosureOf(block))

fun BintrayExtension.PackageConfig.version(block: BintrayExtension.VersionConfig.() -> Unit) =
    version(delegateClosureOf(block))

fun BintrayExtension.VersionConfig.gpg(block: BintrayExtension.GpgConfig.() -> Unit) =
    gpg(delegateClosureOf(block))

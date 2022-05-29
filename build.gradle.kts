import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
//import org.ajoberstar.gradle.git.publish.GitPublishExtension
//import org.ajoberstar.gradle.git.publish.tasks.GitPublishReset
//import org.apache.commons.codec.binary.Base64

buildscript {
    repositories {
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version Versions.kotlin
    id("org.jetbrains.kotlin.kapt") version Versions.kotlin
    id("com.github.johnrengelman.shadow") version Versions.shadow
    id("io.gitlab.arturbosch.detekt") version Versions.detekt

    signing
    `maven-publish`
    id("io.codearte.nexus-staging") version Versions.nexus
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://kotlin.bintray.com/kotlinx")
    jcenter()
}

dependencies {
    api(Dependencies.jdk8)
}


group = Library.group
version = Library.version

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "io.github.arturbosch.detekt")
    if(!isJitPack && Library.isRelease) {
        apply(plugin = "signing")
    }

    repositories {
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        jcenter()
    }

    dependencies {
        api(Dependencies.jdk8)
        api(Dependencies.coroutines)
        api(Dependencies.`kotlin-logging`)
        implementation(Dependencies.`kotlin-poet`)

        testImplementation(Dependencies.mockk)
        testImplementation(Dependencies.`mockk-dsl`)
        testImplementation(Dependencies.`kotlin-test`)
        testImplementation(Dependencies.`kotlin-test-junit`)
        testImplementation(Dependencies.`coroutines-test`)
        testImplementation(Dependencies.`junit-jupiter-api`)
        testImplementation(Dependencies.`junit-jupiter-params`)
        testRuntimeOnly(Dependencies.`junit-jupiter-engine`)
        testRuntimeOnly(Dependencies.`kotlin-reflect`)
        testRuntimeOnly(Dependencies.slf4j)
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                jvmTarget = Jvm.target
                freeCompilerArgs = listOf(
                    CompilerArguments.inlineClasses,
                    CompilerArguments.coroutines,
                    CompilerArguments.experimental
                )
            }
        }

        compileTestKotlin {
            kotlinOptions {
                jvmTarget = Jvm.target
                freeCompilerArgs = listOf(
                    CompilerArguments.inlineClasses,
                    CompilerArguments.coroutines,
                    CompilerArguments.experimental
                )
            }
        }

        test {
            useJUnitPlatform {
                includeEngines("junit-jupiter")
            }
        }

        kotlinSourcesJar {
            archiveClassifier.set("sources")
            from(sourceSets.main.get().allSource)
        }

        apply<BintrayPlugin>()

        publishing {
            publications {
                create<MavenPublication>(Library.name) {
                    //from(components["kotlin"])
                    groupId = Library.group
                    artifactId = project.name
                    version = Library.version

                    artifact(kotlinSourcesJar)
                }
            }
        }

        configure<BintrayExtension> {
            user = System.getenv("BINTRAY_USER")
            key = System.getenv("BINTRAY_KEY")
            setPublications(Library.name)
            publish = true

            pkg = PackageConfig().apply {
                repo = "Kord"
                name = "kordx.commands"
                userOrg = "kordlib"
                setLicenses("MIT")
            vcsUrl = 'https://gitlab.com/kordlib/kordx.commands.git'
            websiteUrl = 'https://gitlab.com/kordlib/kordx.commands'
            issueTrackerUrl = 'https://gitlab.com/kordlib/kordx.commands/issues'

                version = VersionConfig().apply {
                    name = Library.version
                    desc = Library.description
                    vcsTag = Library.version
                }
            }
        }
    }
}

nexusStaging { }
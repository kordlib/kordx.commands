plugins {
    kotlin("jvm") version Versions.kotlin
    id("io.gitlab.arturbosch.detekt") version "1.9.0" apply false
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    kotlin("plugin.serialization") version "1.4.21" apply false

    signing
    `maven-publish`
    id("io.codearte.nexus-staging") version "0.22.0"

}

repositories {
    mavenCentral()
}

group = Library.group
version = Library.version

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "io.gitlab.arturbosch.detekt")


    if (!isJitPack && Library.isRelease) {
        apply(plugin = "signing")
    }

    val kotlinComponent = components["kotlin"]

    repositories {
        mavenCentral()
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
    }
    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }
    val javadocJar by tasks.registering(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
    }


    publishing {
        publications {
            create<MavenPublication>(Library.name) {
                from(components["kotlin"])
                groupId = Library.group
                artifactId = "commands-${project.name}"
                version = Library.version

                artifact(sourcesJar.get())

                pom {
                    name.set(Library.name)
                    description.set(Library.description)
                    url.set(Library.description)

                    organization {
                        name.set("Kord")
                        url.set(Library.url)
                    }

                    developers {
                        developer {
                            name.set("The Kord Team")
                        }
                    }

                    issueManagement {
                        system.set("GitHub")
                        url.set("${Library.url}/issues")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm {
                        connection.set("scm:git:ssh://github.com/kordlib/kordx.commands.git")
                        developerConnection.set("scm:git:ssh://git@github.com:kordlib/kordx.commands.git")
                        url.set(Library.url)
                    }
                }

                if (!isJitPack) {
                    repositories {
                        maven {
                            url = if (Library.isSnapshot) uri(Repo.snapshotsUrl)
                            else uri(Repo.releasesUrl)

                            credentials {
                                username = System.getenv("NEXUS_USER")
                                password = System.getenv("NEXUS_PASSWORD")
                            }
                        }
                    }
                }

            }
        }

    }
}

if (!isJitPack && Library.isRelease) {
    signing {
        val signingKey = findProperty("signingKey")?.toString()
        val signingPassword = findProperty("signingPassword")?.toString()
        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(
                String(org.apache.commons.codec.binary.Base64().decode(signingKey.toByteArray())),
                signingPassword
            )
        }
        sign(publishing.publications[Library.name])
    }
}



nexusStaging { }
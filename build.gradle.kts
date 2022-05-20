plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    java
    `maven-publish`
    `kotlin-dsl`
    id("io.codearte.nexus-staging") version "0.30.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://kotlin.bintray.com/kotlinx")
}

val kotlinVersion = "1.6.21"
val libVersion = System.getenv("RELEASE_TAG") ?: System.getenv("GITHUB_SHA") ?: System.getenv("VERSION") ?: "undefined"
val libGroup = "dev.kord.x"
val coroutinesVersion = "1.6.1"

val junitVersion = "5.8.2"
val mockkVersion = "1.12.4"
val kotlinLoggingVersion = "2.1.21"
val log4jVersion = "1.7.36"

group = libGroup
version = libVersion

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    testImplementation("io.mockk:mockk-dsl-jvm:$mockkVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:$log4jVersion")
    implementation("com.squareup:kotlinpoet:1.11.0")
}

tasks {
    withType<Test> {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs.plus(
                listOf(
                    "-XXLanguage:+InlineClasses",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xopt-in=kotlin.Experimental"
                )
            )
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs.plus(
                listOf(
                    "-XXLanguage:+InlineClasses",
                    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xopt-in=kotlin.Experimental"
                )
            )
        }
    }

    kotlinSourcesJar {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    publishing {
        publications {
            create<MavenPublication>(libGroup) {
                groupId = libGroup
                //artifactId = //archivesBaseName
                version = libVersion

                //artifact sourcesJar
                pom {
                }
            }
        }
    }
}
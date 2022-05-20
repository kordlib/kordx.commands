plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}

val junitVersion = "5.8.2"
val coroutinesVersion = "1.6.1"

dependencies {
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    api(project(":kordx-commands-runtime"))

    api("dev.kord:kord-core:0.8.0-M14") {
//        version {
//            prefer("latest.release")
//        }
    }

    api("dev.kord.x:emoji:0.5.0") {
        version {
            prefer("latest.release")
        }
    }

    api("info.debatty:java-string-similarity:2.0.0")

    kaptTest(project(":kordx-commands-processor"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs += "-Xuse-experimental=kotlin.ExperimentalStdlibApi"
    }
}
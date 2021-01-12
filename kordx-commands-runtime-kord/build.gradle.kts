plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    jcenter()
}

dependencies {
    implementation(Dependencies.`kotlin-logging`)

    api(runtime)

    api("com.gitlab.kordlib.kord:kord-core:0.6.+") {
        version {
            prefer("latest.release")
        }
    }

    api("com.gitlab.kordlib:kordx.emoji:0.2.+") {
        version {
            prefer("latest.release")
        }
    }

    api("info.debatty:java-string-similarity:1.2.1")

    kaptTest(processor)
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + CompilerArguments.experimentalStdlib
        }
    }
}
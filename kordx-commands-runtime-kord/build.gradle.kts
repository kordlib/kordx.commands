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
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs += "-Xuse-experimental=kotlin.ExperimentalStdlibApi"
    }
}
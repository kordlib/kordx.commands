plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    jcenter()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(Dependencies.`kotlin-logging`)

    api(runtime)

    api(Dependencies.kord) {
//        version {
//            prefer("latest.release")
//        }
    }

// Waiting for https://github.com/kordlib/kordx.emoji/pull/4 to release as kx.emoji
// would add an outated version of kord to the classpath
//    api(Dependencies.kordxEmoji) {
//        version {
//            prefer("latest.release")
//        }
//    }

    api(Dependencies.javaStringSimilarity)

    kaptTest(processor)
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + CompilerArguments.experimentalStdlib
        }
    }
}
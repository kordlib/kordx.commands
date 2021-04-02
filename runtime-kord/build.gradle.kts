plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
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

    api(Dependencies.kordxEmoji) {
//        version {
//            prefer("latest.release")
//        }
    }

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
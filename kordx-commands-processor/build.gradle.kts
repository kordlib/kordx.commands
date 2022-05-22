plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":kordx-commands-runtime"))
    kaptTest(project(":kordx-commands-processor"))
    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.google.auto.service:auto-service:1.0.1")
    testImplementation(project(":kordx-commands-runtime-kord"))
    implementation("com.squareup:kotlinpoet:1.11.0")
}

kapt {
    arguments {
        arg("kordx.commands.verbose", "true")
    }
}
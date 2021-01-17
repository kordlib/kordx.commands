plugins {
    groovy
    `kotlin-dsl`
}

group = "dev.kord.x"
version = "0.4.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin-api", version = "1.4.0"))
    implementation(gradleApi())
    implementation(localGroovy())
}

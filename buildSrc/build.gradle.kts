plugins {
    groovy
    `kotlin-dsl`
}

group = "dev.kord.x"
version = "undefined"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin-api", version = "1.4.0"))
    implementation(gradleApi())
    implementation(localGroovy())
}

plugins {
    groovy
    `kotlin-dsl`
}

group = "com.gitlab.kordlib.kordx"
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

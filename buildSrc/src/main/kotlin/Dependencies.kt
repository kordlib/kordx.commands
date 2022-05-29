object Versions {
    const val kotlin = "1.6.21"
    const val kordRange = "0.8.+"
    const val nexus = "0.30.0"
    const val shadow = "7.1.2"
    const val detekt = "1.20.0"
    const val bintray = "1.8.5"
    const val mockk = "1.12.4"
    const val junit = "5.8.2"
    const val log4j = "1.7.36"
    const val koin = "3.2.0"
    const val kotlinxSerialization = "1.3.3"
    const val kotlinxCoroutines = "1.6.2"
    const val kotlinLogging = "2.1.23"
    const val kotlinPoet = "1.11.0"
}

object Dependencies {
    const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val `kotlinx-serialization`= "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val `kotlin-logging` = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"
    const val `kotlin-poet` = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"

    // Test dependencies
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val `mockk-dsl` = "io.mockk:mockk-dsl-jvm:${Versions.mockk}"
    const val `kotlin-test` = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    const val `kotlin-test-junit` = "org.jetbrains.kotlin:kotlin-test-junit5:1.6.21"
    const val `coroutines-test` = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutines}"
    const val `kotlin-reflect` = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val `junit-jupiter-api` = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
    const val `junit-jupiter-engine` = "org.junit.jupiter:junit-jupiter-params:${Versions.junit}"
    const val `junit-jupiter-params` = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
    const val slf4j = "org.slf4j:slf4j-simple:${Versions.log4j}"
}
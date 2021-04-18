object Versions {
    const val kotlin = "1.4.21"
    const val kotlinxCoroutines = "1.3.9"
    const val koin = "2.1.6"
    const val junit = "5.6.0"
    const val mockk = "1.10.0"
    const val kotlinLogging = "1.8.3"
    const val log4j = "1.7.26"
    const val kotlinPoet = "1.4.1"

    const val kotlinTest = kotlin
    const val junit5 = "5.6.0"
    const val kotlinxCoroutinesTest = kotlinxCoroutines
    const val kotlinReflect = kotlin
    const val junitJupiterApi = junit5
    const val junitJupiterParams = junit5
    const val junitJupiterEngine = junit5
    const val sl4j = "1.7.30"
    const val javaStringSimilarity = "1.2.1"

    const val kord = "0.7.x-SNAPSHOT"
    const val kordxEmoji = "0.5.0-SNAPSHOT"
}

object Dependencies {

    const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val `kotlinx-coroutines` =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"

    const val `kotlin-logging` = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"


    const val koin = "org.koin:koin-core:${Versions.koin}"

    const val `kotlin-test` = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlinTest}"
    const val `kotlinx-coroutines-test` =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutinesTest}"
    const val `kotlin-reflect` = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinReflect}"
    const val junit5 = "org.jetbrains.kotlin:kotlin-test-junit5:${Versions.kotlinTest}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val mockkDsl = "io.mockk:mockk-dsl-jvm:${Versions.mockk}"
    const val `junit-jupiter-api` =
        "org.junit.jupiter:junit-jupiter-api:${Versions.junitJupiterApi}"
    const val `junit-jupiter-params` =
        "org.junit.jupiter:junit-jupiter-params:${Versions.junitJupiterParams}"
    const val `junit-jupiter-engine` =
        "org.junit.jupiter:junit-jupiter-engine:${Versions.junitJupiterEngine}"
    const val sl4j = "org.slf4j:slf4j-simple:${Versions.sl4j}"

    const val kotlinPoet = "com.squareup:kotlinpoet:${Versions.kotlinPoet}"

    const val javaStringSimilarity =
        "info.debatty:java-string-similarity:${Versions.javaStringSimilarity}"

    const val kord = "dev.kord:kord-core:${Versions.kord}"
    const val kordxEmoji = "dev.kord.x:emoji:${Versions.kordxEmoji}"

}

plugins {
    `kotlin-dsl`
}
val koin_version = "3.2.0"
val coroutinesVersion = "1.6.1"
val kotlinLoggingVersion = "2.1.21"
repositories {
    mavenCentral()
}
dependencies {
    api("io.insert-koin:koin-core:$koin_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
}
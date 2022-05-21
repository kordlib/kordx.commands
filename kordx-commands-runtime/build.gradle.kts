plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
}
val koin_version = "3.2.0"
val coroutinesVersion = "1.6.1"
val kotlinLoggingVersion = "2.1.21"
val junitVersion = "5.8.2"
val kotlinVersion = "1.6.21"
val mockkVersion = "1.12.4"
val log4jVersion = "1.7.36"
repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}
dependencies {
    api("io.insert-koin:koin-core:$koin_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    testImplementation("io.mockk:mockk-dsl-jvm:$mockkVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:$log4jVersion")
    implementation("com.squareup:kotlinpoet:1.11.0")
}
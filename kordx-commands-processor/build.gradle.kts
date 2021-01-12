plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    jcenter()
}

dependencies {
    implementation(runtime)
    kaptTest(processor)
    kapt("com.google.auto.service:auto-service:1.0-rc6")
    implementation("com.google.auto.service:auto-service:1.0-rc6")
    testImplementation(`runtime-kord`)
}

kapt {
    arguments {
        arg("kordx.commands.verbose", "true")
    }
}

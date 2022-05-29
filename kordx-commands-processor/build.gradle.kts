dependencies {
    implementation(project(":kordx-commands-runtime"))
    kaptTest(project(":kordx-commands-processor"))
    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.google.auto.service:auto-service:1.0.1")
    testImplementation(project(":kordx-commands-runtime-kord"))
}

kapt {
    arguments {
        arg("kordx.commands.verbose", "true")
    }
}
group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //// Production Dependencies
    // Implementation
    implementation("io.github.oshai:kotlin-logging:6.0.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.springframework.boot:spring-boot-starter")
    //// Test Dependencies
    // Implementation
    testImplementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}
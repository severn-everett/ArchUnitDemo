group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val metadataVersion: String by properties
    //// Production Dependencies
    // Implementation
    implementation("io.github.oshai:kotlin-logging:5.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.springframework.boot:spring-boot-starter")
    //// Test Dependencies
    // Implementation
    testImplementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:$metadataVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}
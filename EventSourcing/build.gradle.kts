group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val coroutinesVersion: String by properties
    val metadataVersion: String by properties
    //// Production Dependencies
    // Implementation
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Runtime
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
    //// Test Dependencies
    // Implementation
    testImplementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:$metadataVersion")
}

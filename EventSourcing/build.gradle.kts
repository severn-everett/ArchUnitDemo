plugins {
    kotlin("plugin.serialization") version "2.0.0"
}

group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //// Production Dependencies
    // Implementation
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Runtime
    runtimeOnly(kotlin("reflect"))
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    //// Test Dependencies
    // Implementation
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}

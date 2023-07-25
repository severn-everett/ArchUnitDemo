plugins {
    kotlin("plugin.jpa") version "1.9.0"
}

apply(plugin = "kotlin-jpa")

group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //// Production Dependencies
    // Implementation
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Runtime
    runtimeOnly(kotlin("reflect"))
    runtimeOnly("com.h2database:h2:2.2.220")
    //// Testing Dependencies
}

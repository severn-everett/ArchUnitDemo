plugins {
    val kotlinVersion = "1.8.10"
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-spring")

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
    runtimeOnly("com.h2database:h2:2.1.214")
    //// Testing Dependencies
    // Implementation
    testImplementation("com.tngtech.archunit:archunit:1.0.1")
}

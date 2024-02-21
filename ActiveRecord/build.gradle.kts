plugins {
    kotlin("plugin.jpa") version "1.9.22"
}

apply(plugin = "kotlin-jpa")

group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val archUnitVersion: String by properties
    val h2Version: String by properties
    //// Production Dependencies
    // Implementation
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Runtime
    runtimeOnly(kotlin("reflect"))
    runtimeOnly("com.h2database:h2:$h2Version")
    //// Testing Dependencies
    // Implementation
    testImplementation("com.tngtech.archunit:archunit-junit5:$archUnitVersion")
}

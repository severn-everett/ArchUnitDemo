plugins {
    kotlin("jvm") version "1.8.10"
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")
    kotlin {
        jvmToolchain(19)
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }

    dependencies {
        testImplementation(kotlin("test"))
        testImplementation("com.tngtech.archunit:archunit:1.0.1")
    }
}
plugins {
    val kotlinVersion = "1.9.22"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")
    kotlin {
        jvmToolchain(21)
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }

    dependencies {
        val archUnitVersion: String by properties
        testImplementation(kotlin("test"))
        testImplementation("com.tngtech.archunit:archunit:$archUnitVersion")
    }
}

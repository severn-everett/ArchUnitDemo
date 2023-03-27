plugins {
    kotlin("jvm") version "1.8.10"
}

group = "com.severett"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
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
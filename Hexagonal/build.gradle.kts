group = "com.severett.archunitdemo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.oshai:kotlin-logging:5.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")
}
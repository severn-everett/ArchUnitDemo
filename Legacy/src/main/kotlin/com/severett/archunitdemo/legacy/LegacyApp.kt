package com.severett.archunitdemo.legacy

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LegacyApp

fun main(args: Array<String>) {
    SpringApplication.run(LegacyApp::class.java, *args)
}

package com.severett.archunitdemo.hexagonal

import com.severett.archunitdemo.hexagonal.service.AnalyzerService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class HexagonalApp(private val analyzerService: AnalyzerService) : CommandLineRunner {
    override fun run(vararg args: String) {
        analyzerService.run()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(HexagonalApp::class.java, *args)
}

package com.severett.archunitdemo.hexagonal

import com.severett.archunitdemo.hexagonal.usecase.ValidationUseCase
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class HexagonalApp(private val validationUseCase: ValidationUseCase) : CommandLineRunner {
    override fun run(vararg args: String) {
        validationUseCase.run()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(HexagonalApp::class.java, *args)
}

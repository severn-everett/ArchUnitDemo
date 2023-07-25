package com.severett.archunitdemo.hexagonal.usecase

import com.severett.archunitdemo.hexagonal.port.ListenerPort
import com.severett.archunitdemo.hexagonal.port.ReporterPort
import com.severett.archunitdemo.hexagonal.service.AnalyzerService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger { }

@Component
class ValidationUseCase(
    private val listenerPort: ListenerPort,
    private val analyzerService: AnalyzerService,
    private val reporterPort: ReporterPort
) {
    fun run() {
        runBlocking {
            listenerPort.listen().collect { ccNumber ->
                logger.info { "Received number: $ccNumber" }
                reporterPort.reportNumberResult(analyzerService.analyzeCreditCardNumber(ccNumber))
            }
        }
    }
}

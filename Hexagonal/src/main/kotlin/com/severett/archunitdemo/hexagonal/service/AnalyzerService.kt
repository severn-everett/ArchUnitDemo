package com.severett.archunitdemo.hexagonal.service

import com.severett.archunitdemo.hexagonal.port.ListenerPort
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger { }

@Service
class AnalyzerService(private val listenerPort: ListenerPort) {
    fun run() {
        runBlocking {
            listenerPort.listen().collect { ccNumber ->
                logger.info { "Received number: $ccNumber" }
            }
        }
    }
}

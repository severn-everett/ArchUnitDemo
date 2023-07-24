package com.severett.archunitdemo.hexagonal.adapter

import com.severett.archunitdemo.hexagonal.domain.ValidationResult
import com.severett.archunitdemo.hexagonal.port.ReporterPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger { }

@Service
class MongoReporterAdapter : ReporterPort {
    override fun reportNumberResult(result: ValidationResult) {
        logger.info { "Reporting instance of ${result.javaClass} for CC Number ${result.ccNumber}" }
    }
}

package com.severett.archunitdemo.hexagonal.adapter

import com.severett.archunitdemo.hexagonal.domain.CreditCardIssuer
import com.severett.archunitdemo.hexagonal.port.ListenerPort
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.*
import kotlin.random.Random

private val logger = KotlinLogging.logger { }

@Component
class GeneratorListenerAdapter : ListenerPort {
    private val isRunning = AtomicBoolean(true)
    private val creditCardIssuers = CreditCardIssuer.values()

    @PostConstruct
    fun postConstruct() {
        Runtime.getRuntime().addShutdownHook(
            object : Thread() {
                override fun run() {
                    logger.info { "Shutting down generator" }
                    isRunning.set(false)
                }
            }
        )
    }

    override fun listen(): Flow<ULong> {
        return flow {
            while (isRunning.get()) {
                val issuer = creditCardIssuers[Random.nextInt(0, creditCardIssuers.size)]
                logger.info { "Generating number for '${issuer.brand}'" }
                emit(generateNumber(issuer.initialNums, issuer.limit))
                delay(1000L)
            }
        }
    }

    private fun generateNumber(initialNums: Int, limit: Int): ULong {
        val generatedSuffix = (0 until limit).map { Random.nextInt(0, 10) }.joinToString("")
        return "$initialNums$generatedSuffix".toULong()
    }
}

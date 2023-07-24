package com.severett.archunitdemo.hexagonal.adapter

import com.severett.archunitdemo.hexagonal.domain.CreditCardIssuer
import com.severett.archunitdemo.hexagonal.port.ListenerPort
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.*
import kotlin.random.Random

private val logger = KotlinLogging.logger { }
private val UNKNOWN_INITIAL_NUMS = listOf(9u, 9u, 9u, 9u)
private const val UNKNOWN_LIMIT = 10

@Component
internal class GeneratorListenerAdapter : ListenerPort {
    private val isRunning = AtomicBoolean(true)
    private val creditCardIssuers = CreditCardIssuer.entries

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
                val ccNumber = Random
                    .nextInt(0, creditCardIssuers.size + 1)
                    .takeIf { it < creditCardIssuers.size }
                    ?.let { issuerIndex ->
                        val issuer = creditCardIssuers[issuerIndex]
                        logger.info { "Generating number for '${issuer.brand}'" }
                        generateNumber(issuer.initialNums, issuer.limit)
                    } ?: run {
                    logger.info { "Generating number for unknown issuer" }
                    generateNumber(UNKNOWN_INITIAL_NUMS, UNKNOWN_LIMIT)
                }
                emit(ccNumber)
                delay(1000L)
            }
        }
    }

    private fun generateNumber(initialNums: List<UInt>, limit: Int): ULong {
        val generatedSuffix = (0..<(limit - initialNums.size))
            .map { Random.nextInt(0, 10) }
            .joinToString("")
        return "${initialNums.joinToString("")}$generatedSuffix".toULong()
    }
}

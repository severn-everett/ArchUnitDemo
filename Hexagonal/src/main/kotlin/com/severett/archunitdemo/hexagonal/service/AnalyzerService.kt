package com.severett.archunitdemo.hexagonal.service

import com.severett.archunitdemo.hexagonal.domain.CreditCardIssuer
import com.severett.archunitdemo.hexagonal.domain.ValidationResult
import com.severett.archunitdemo.hexagonal.port.ListenerPort
import com.severett.archunitdemo.hexagonal.port.ReporterPort
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.time.Instant

private val logger = KotlinLogging.logger { }

@Service
class AnalyzerService(private val listenerPort: ListenerPort, private val reporterPort: ReporterPort) {
    private val creditCardIssuers = CreditCardIssuer.entries

    fun run() {
        runBlocking {
            listenerPort.listen().collect(::analyzeCreditCardNumber)
        }
    }

    private fun analyzeCreditCardNumber(ccNumber: ULong) {
        logger.info { "Received number: $ccNumber" }
        val ccDigits = toDigitList(ccNumber)
        val issuer = getCreditCardIssuer(ccDigits)
        val result = when {
            issuer == null -> ValidationResult.InvalidIssuerFailure(ccNumber, Instant.now())
            !isValidNumber(ccDigits) -> ValidationResult.InvalidNumberFailure(issuer.brand, ccNumber, Instant.now())
            else -> ValidationResult.Success(issuer.brand, ccNumber, Instant.now())
        }
        reporterPort.reportNumberResult(result)
    }

    private fun toDigitList(ccNumber: ULong): List<UInt> {
        var number = ccNumber
        val digitList = mutableListOf<UInt>()
        do {
            digitList.add((number % 10u).toUInt())
            number /= 10u
        } while (number > 0u)
        return digitList.reversed()
    }

    private fun getCreditCardIssuer(ccDigits: List<UInt>) = creditCardIssuers.firstOrNull { issuer ->
        logger.debug {
            "Check for ${issuer.brand}: ${issuer.limit} | ${ccDigits.size} | " +
                    "${ccDigits.subList(0, issuer.initialNums.size)} | ${issuer.initialNums}"
        }
        (issuer.limit) == ccDigits.size && ccDigits.subList(0, issuer.initialNums.size) == issuer.initialNums
    }

    private fun isValidNumber(ccStr: List<UInt>): Boolean {
        val sum = (ccStr.indices).fold(0u) { sum, i ->
            val index = (ccStr.size - 1) - i
            if ((i + 1) % 2 == 1) {
                sum + ccStr[index]
            } else {
                val digit = ccStr[index]
                sum + if (digit < 5u) digit * 2u else (2u * (5u - digit)) - 1u
            }
        }
        logger.info { "Summed Number: $sum" }
        return (sum % 10u) == 0u
    }
}

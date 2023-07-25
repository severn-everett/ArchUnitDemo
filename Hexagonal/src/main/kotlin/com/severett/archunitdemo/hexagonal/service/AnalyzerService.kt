package com.severett.archunitdemo.hexagonal.service

import com.severett.archunitdemo.hexagonal.domain.CreditCardIssuer
import com.severett.archunitdemo.hexagonal.domain.ValidationResult
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant

private val logger = KotlinLogging.logger { }

@Service
class AnalyzerService {
    private val creditCardIssuers = CreditCardIssuer.entries

    fun analyzeCreditCardNumber(ccNumber: ULong): ValidationResult {
        val ccDigits = toDigitList(ccNumber)
        val issuer = getCreditCardIssuer(ccDigits)
        return when {
            issuer == null -> ValidationResult.InvalidIssuerFailure(ccNumber, Instant.now())
            !isValidNumber(ccDigits) -> ValidationResult.InvalidNumberFailure(issuer.brand, ccNumber, Instant.now())
            else -> ValidationResult.Success(issuer.brand, ccNumber, Instant.now())
        }
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
        logger.debug { "Summed Number: $sum" }
        return (sum % 10u) == 0u
    }
}

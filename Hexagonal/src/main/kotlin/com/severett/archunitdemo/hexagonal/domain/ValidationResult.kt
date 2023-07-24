package com.severett.archunitdemo.hexagonal.domain

import java.time.Instant

sealed class ValidationResult(val isSuccess: Boolean, val ccNumber: ULong, val timestamp: Instant) {
    class Success(
        val issuer: String,
        ccNumber: ULong,
        timestamp: Instant
    ) : ValidationResult(true, ccNumber, timestamp)

    class InvalidIssuerFailure(
        ccNumber: ULong,
        timestamp: Instant
    ) : ValidationResult(false, ccNumber, timestamp) {
        val reason = "Unrecognized Issuer"
    }

    class InvalidNumberFailure(
        val issuer: String,
        ccNumber: ULong,
        timestamp: Instant
    ) : ValidationResult(true, ccNumber, timestamp) {
        val reason = "Invalid Number"
    }
}
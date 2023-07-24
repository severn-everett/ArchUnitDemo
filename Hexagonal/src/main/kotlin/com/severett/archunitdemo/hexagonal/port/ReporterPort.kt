package com.severett.archunitdemo.hexagonal.port

import com.severett.archunitdemo.hexagonal.domain.ValidationResult

interface ReporterPort {
    fun reportNumberResult(result: ValidationResult)
}

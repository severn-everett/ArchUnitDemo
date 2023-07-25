package com.severett.archunitdemo.eventsourcing.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class StockAmountDto(val sku: String, val quantity: UInt?)

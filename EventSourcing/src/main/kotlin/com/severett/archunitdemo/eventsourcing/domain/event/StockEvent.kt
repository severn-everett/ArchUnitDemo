package com.severett.archunitdemo.eventsourcing.domain.event

import java.time.Instant

sealed interface StockEvent {
    val sku: String
}

data class StockReceivedEvent(override val sku: String, val quantity: UInt, val timestamp: Instant) : StockEvent
data class StockShippedEvent(override val sku: String, val quantity: UInt, val timestamp: Instant) : StockEvent

enum class StockEventType {
    RECEIVED, SHIPPED
}

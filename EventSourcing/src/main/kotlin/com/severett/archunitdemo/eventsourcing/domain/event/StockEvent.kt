package com.severett.archunitdemo.eventsourcing.domain.event

import java.time.Instant

sealed interface StockEvent

data class StockReceivedEvent(val quantity: UInt, val timestamp: Instant) : StockEvent
data class StockShippedEvent(val quantity: UInt, val timestamp: Instant) : StockEvent

enum class StockEventType {
    RECEIVED, SHIPPED
}

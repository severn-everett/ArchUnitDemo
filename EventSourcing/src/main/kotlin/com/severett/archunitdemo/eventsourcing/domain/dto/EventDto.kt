package com.severett.archunitdemo.eventsourcing.domain.dto

import com.severett.archunitdemo.eventsourcing.domain.event.StockEventType
import com.severett.archunitdemo.eventsourcing.serde.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class EventDto(
    val type: StockEventType,
    val quantity: UInt,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant
)

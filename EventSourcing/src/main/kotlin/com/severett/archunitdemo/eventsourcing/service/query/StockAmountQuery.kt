package com.severett.archunitdemo.eventsourcing.service.query

import com.severett.archunitdemo.eventsourcing.domain.dto.StockAmountDto
import com.severett.archunitdemo.eventsourcing.domain.event.StockReceivedEvent
import com.severett.archunitdemo.eventsourcing.domain.event.StockShippedEvent
import com.severett.archunitdemo.eventsourcing.repo.ReadRepo
import org.springframework.stereotype.Service

@Service
class StockAmountQuery(private val readRepo: ReadRepo) {
    suspend fun execute(sku: String): StockAmountDto {
        val summedQuantity = readRepo.getEvents(sku)?.fold(0u) { quantity, stockEvent ->
            when (stockEvent) {
                is StockReceivedEvent -> quantity + stockEvent.quantity
                is StockShippedEvent -> quantity - stockEvent.quantity
            }
        }?.takeIf { it > UInt.MIN_VALUE }
        return StockAmountDto(sku, summedQuantity)
    }
}

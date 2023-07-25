package com.severett.archunitdemo.eventsourcing.service.command

import com.severett.archunitdemo.eventsourcing.domain.event.StockShippedEvent
import com.severett.archunitdemo.eventsourcing.repo.WriteRepo
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SaveShippedCommand(private val writeRepo: WriteRepo) {
    suspend fun execute(sku: String, quantity: UInt) {
        writeRepo.saveEvent(sku, StockShippedEvent(quantity, Instant.now()))
    }
}

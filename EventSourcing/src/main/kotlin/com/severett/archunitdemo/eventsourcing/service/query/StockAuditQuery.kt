package com.severett.archunitdemo.eventsourcing.service.query

import com.severett.archunitdemo.eventsourcing.domain.dto.EventDto
import com.severett.archunitdemo.eventsourcing.domain.event.StockEventType
import com.severett.archunitdemo.eventsourcing.domain.event.StockReceivedEvent
import com.severett.archunitdemo.eventsourcing.domain.event.StockShippedEvent
import com.severett.archunitdemo.eventsourcing.repo.ReadRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service

@Service
class StockAuditQuery(private val readRepo: ReadRepo) {
    suspend fun execute(sku: String): Flow<EventDto> {
        return readRepo.getEvents(sku)?.let { events ->
            flow {
                events.forEach { event ->
                    when (event) {
                        is StockReceivedEvent -> {
                            emit(EventDto(StockEventType.RECEIVED, event.quantity, event.timestamp))
                        }
                        is StockShippedEvent -> {
                            emit(EventDto(StockEventType.SHIPPED, event.quantity, event.timestamp))
                        }
                    }
                }
            }
        } ?: emptyFlow()
    }
}

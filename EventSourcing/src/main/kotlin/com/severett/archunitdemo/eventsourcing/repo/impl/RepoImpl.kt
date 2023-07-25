package com.severett.archunitdemo.eventsourcing.repo.impl

import com.severett.archunitdemo.eventsourcing.domain.event.StockEvent
import com.severett.archunitdemo.eventsourcing.domain.event.StockReceivedEvent
import com.severett.archunitdemo.eventsourcing.domain.event.StockShippedEvent
import com.severett.archunitdemo.eventsourcing.repo.ReadRepo
import com.severett.archunitdemo.eventsourcing.repo.WriteRepo
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.temporal.ChronoUnit

@Repository
class RepoImpl : ReadRepo, WriteRepo {
    private val mutex = Mutex()
    private val eventsMap = mutableMapOf<String, MutableList<StockEvent>>()

    @PostConstruct
    fun postConstruct() {
        // Seed eventsMap with events for SKU `ABC123`
        val now = Instant.now()
        eventsMap["ABC123"] = mutableListOf(
            StockReceivedEvent(25u, now.minus(4, ChronoUnit.DAYS)),
            StockShippedEvent(20u, now.minus(3, ChronoUnit.DAYS)),
            StockReceivedEvent(30u, now.minus(2, ChronoUnit.DAYS)),
            StockShippedEvent(15u, now.minus(1, ChronoUnit.DAYS)),
        )
    }

    override suspend fun getEvents(sku: String) = mutex.withLock { eventsMap[sku] }

    override suspend fun saveEvent(sku: String, event: StockEvent) {
        mutex.withLock {
            eventsMap.getOrPut(sku, ::mutableListOf).add(event)
        }
    }
}

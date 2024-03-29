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
        val sku = "ABC123"
        eventsMap[sku] = mutableListOf(
            StockReceivedEvent(sku, 25u, now.minus(4, ChronoUnit.DAYS)),
            StockShippedEvent(sku, 20u, now.minus(3, ChronoUnit.DAYS)),
            StockReceivedEvent(sku, 30u, now.minus(2, ChronoUnit.DAYS)),
            StockShippedEvent(sku, 15u, now.minus(1, ChronoUnit.DAYS)),
        )
    }

    override suspend fun getEvents(sku: String) = mutex.withLock { eventsMap[sku] }

    override suspend fun saveEvent(event: StockEvent) {
        mutex.withLock {
            eventsMap.getOrPut(event.sku, ::mutableListOf).add(event)
        }
    }
}

package com.severett.archunitdemo.eventsourcing.repo

import com.severett.archunitdemo.eventsourcing.domain.event.StockEvent

interface ReadRepo {
    suspend fun getEvents(sku: String): List<StockEvent>?
}

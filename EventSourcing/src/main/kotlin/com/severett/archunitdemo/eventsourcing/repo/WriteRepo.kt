package com.severett.archunitdemo.eventsourcing.repo

import com.severett.archunitdemo.eventsourcing.domain.event.StockEvent

interface WriteRepo {
    suspend fun saveEvent(event: StockEvent)
}

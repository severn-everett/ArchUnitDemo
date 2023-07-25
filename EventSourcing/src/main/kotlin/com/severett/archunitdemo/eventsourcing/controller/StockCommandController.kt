package com.severett.archunitdemo.eventsourcing.controller

import com.severett.archunitdemo.eventsourcing.service.command.SaveReceivedCommand
import com.severett.archunitdemo.eventsourcing.service.command.SaveShippedCommand
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock/command")
class StockCommandController(
    private val saveReceivedCommand: SaveReceivedCommand,
    private val saveShippedCommand: SaveShippedCommand,
) {
    @PostMapping("/{sku}/received")
    suspend fun saveReceived(@PathVariable sku: String, @RequestParam quantity: UInt) {
        saveReceivedCommand.execute(sku, quantity)
    }

    @PostMapping("/{sku}/shippped")
    suspend fun saveShipped(@PathVariable sku: String, @RequestParam quantity: UInt) {
        saveShippedCommand.execute(sku, quantity)
    }
}

package com.severett.archunitdemo.eventsourcing.controller.query

import com.severett.archunitdemo.eventsourcing.service.query.StockAmountQuery
import com.severett.archunitdemo.eventsourcing.service.query.StockAuditQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock/query")
class StockQueryController(
    private val stockAmountQuery: StockAmountQuery,
    private val stockAuditQuery: StockAuditQuery
) {
    @GetMapping("/{sku}/amount")
    suspend fun getAmount(@PathVariable sku: String) = stockAmountQuery.execute(sku)

    @GetMapping("/{sku}/audit")
    suspend fun getAudit(@PathVariable sku: String) = stockAuditQuery.execute(sku)
}

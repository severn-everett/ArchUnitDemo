package com.severett.archunitdemo.activerecord.controller

import com.severett.archunitdemo.activerecord.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestParam ownerId: Long) {
        accountService.createAccount(ownerId)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAccount(@PathVariable id: Long) = accountService.getAccount(id)

    @PostMapping("/{id}/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addAmount(@PathVariable id: Long, @RequestParam amount: BigDecimal) {
        accountService.modifyBalance(id, amount)
    }

    @PostMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeAmount(@PathVariable id: Long, @RequestParam amount: BigDecimal) {
        accountService.modifyBalance(id, amount.negate())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(@PathVariable id: Long) {
        accountService.deleteAccount(id)
    }
}

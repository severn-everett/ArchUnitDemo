package com.severett.archunitdemo.legacy.service

import com.severett.archunitdemo.legacy.controller.AccountDBAccess
import com.severett.archunitdemo.legacy.controller.AccountDTO
import com.severett.archunitdemo.legacy.controller.AccountService
import com.severett.archunitdemo.legacy.model.AccountType
import com.severett.archunitdemo.legacy.model.CreateAccountDTO
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService, private val accountDBAccess: AccountDBAccess) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestBody accountDTO: CreateAccountDTO) {
        accountService.createAccount(accountDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAccount(@PathVariable id: Long) = accountDBAccess.findByIdOrNull(id)
        ?.let { account ->
            AccountDTO(account.id, account.balance, account.type, account.owner.id)
        } ?: throw EntityNotFoundException("No account found for id $id")

    @GetMapping("/byType/{type}")
    @ResponseStatus(HttpStatus.OK)
    fun getAllByType(@PathVariable type: AccountType) = accountService.getByType(type)

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

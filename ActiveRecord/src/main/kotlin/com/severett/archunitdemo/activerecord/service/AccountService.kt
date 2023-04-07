package com.severett.archunitdemo.activerecord.service

import com.severett.archunitdemo.activerecord.model.domain.Account
import com.severett.archunitdemo.activerecord.model.domain.AccountType
import com.severett.archunitdemo.activerecord.model.dto.AccountDTO
import com.severett.archunitdemo.activerecord.model.dto.CreateAccountDTO
import com.severett.archunitdemo.activerecord.model.exception.IllegalOperationException
import com.severett.archunitdemo.activerecord.repo.AccountRepo
import com.severett.archunitdemo.activerecord.repo.OwnerRepo
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountService(private val accountRepo: AccountRepo, private val ownerRepo: OwnerRepo) {
    @Transactional
    fun createAccount(accountDTO: CreateAccountDTO) {
        val account = Account(owner = ownerRepo.getReferenceById(accountDTO.ownerId), type = accountDTO.type)
        accountRepo.save(account)
    }

    fun getAccount(id: Long) = accountRepo.findByIdOrNull(id)
        ?.let { account ->
            AccountDTO(account.id, account.balance, account.type, account.owner.id)
        } ?: throw EntityNotFoundException("No account found for id $id")

    fun getByType(type: AccountType) = accountRepo.getAccounts(type).map { account ->
        AccountDTO(account.id, account.balance, account.type, account.owner.id)
    }

    @Transactional
    fun modifyBalance(id: Long, amount: BigDecimal) {
        val account = accountRepo.getReferenceById(id)
        val newBalance = account.balance.add(amount)
        if (newBalance < BigDecimal.ZERO) {
            throw IllegalOperationException("Unable to aggregate $amount to account $id")
        }
        account.balance = newBalance
        accountRepo.save(account)
    }

    @Transactional
    fun deleteAccount(id: Long) {
        accountRepo.deleteById(id)
    }
}

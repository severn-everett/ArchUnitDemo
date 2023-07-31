package com.severett.archunitdemo.legacy.controller

import com.severett.archunitdemo.legacy.model.Account
import com.severett.archunitdemo.legacy.model.AccountType
import com.severett.archunitdemo.legacy.model.CreateAccountDTO
import com.severett.archunitdemo.legacy.model.IllegalOperationException
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountService(private val accountDBAccess: AccountDBAccess, private val ownerDBAccess: OwnerDBAccess) {
    @Transactional
    fun createAccount(accountDTO: CreateAccountDTO) {
        val account = Account(owner = ownerDBAccess.getReferenceById(accountDTO.ownerId), type = accountDTO.type)
        accountDBAccess.save(account)
    }

    fun getByType(type: AccountType) = accountDBAccess.getAccounts(type).map { account ->
        AccountDTO(account.id, account.balance, account.type, account.owner.id)
    }

    @Transactional
    fun modifyBalance(id: Long, amount: BigDecimal) {
        val account = accountDBAccess.getReferenceById(id)
        val newBalance = account.balance.add(amount)
        if (newBalance < BigDecimal.ZERO) {
            throw IllegalOperationException("Unable to aggregate $amount to account $id")
        }
        account.balance = newBalance
        accountDBAccess.save(account)
    }

    @Transactional
    fun deleteAccount(id: Long) {
        accountDBAccess.deleteById(id)
    }
}

@Repository
interface AccountDBAccess : JpaRepository<Account, Long> {
    @Query("FROM Account a WHERE a.type = :type")
    fun getAccounts(type: AccountType): List<Account>
}

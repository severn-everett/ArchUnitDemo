package com.severett.archunitdemo.activerecord.repo

import com.severett.archunitdemo.activerecord.model.domain.Account
import com.severett.archunitdemo.activerecord.model.domain.AccountType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AccountRepo : JpaRepository<Account, Long> {
    @Query("FROM Account a WHERE a.type = :type")
    fun getAccounts(type: AccountType): List<Account>
}

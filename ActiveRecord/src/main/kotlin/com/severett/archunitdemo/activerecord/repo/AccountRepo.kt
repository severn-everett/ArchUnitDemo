package com.severett.archunitdemo.activerecord.repo

import com.severett.archunitdemo.activerecord.model.domain.Account
import com.severett.archunitdemo.activerecord.model.domain.AccountType
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepo : JpaRepository<Account, Long> {
    fun getByType(type: AccountType): List<Account>
}

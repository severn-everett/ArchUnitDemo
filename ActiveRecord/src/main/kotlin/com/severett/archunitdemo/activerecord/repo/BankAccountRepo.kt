package com.severett.archunitdemo.activerecord.repo

import com.severett.archunitdemo.activerecord.model.domain.BankAccount
import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepo : JpaRepository<BankAccount, Long>

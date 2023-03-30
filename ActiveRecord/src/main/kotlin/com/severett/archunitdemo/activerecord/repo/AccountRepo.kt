package com.severett.archunitdemo.activerecord.repo

import com.severett.archunitdemo.activerecord.model.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepo : JpaRepository<Account, Long>

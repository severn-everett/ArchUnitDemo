package com.severett.archunitdemo.activerecord.model.dto

import com.severett.archunitdemo.activerecord.model.domain.AccountType
import java.math.BigDecimal

data class AccountDTO(val id: Long, val balance: BigDecimal, val type: AccountType, val owner: Long)

package com.severett.archunitdemo.legacy.controller

import com.severett.archunitdemo.legacy.model.AccountType
import java.math.BigDecimal

data class AccountDTO(val id: Long, val balance: BigDecimal, val type: AccountType, val owner: Long)

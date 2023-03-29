package com.severett.archunitdemo.activerecord.model.dto

import java.math.BigDecimal

data class AccountDTO(val id: Long, val amount: BigDecimal, val owner: Long)

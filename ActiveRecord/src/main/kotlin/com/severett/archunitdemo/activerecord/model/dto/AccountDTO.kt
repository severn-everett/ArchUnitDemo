package com.severett.archunitdemo.activerecord.model.dto

import java.math.BigDecimal

data class AccountDTO(val id: Long, val balance: BigDecimal, val owner: Long)

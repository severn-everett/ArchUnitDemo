package com.severett.archunitdemo.activerecord.model.dto

import com.severett.archunitdemo.activerecord.model.domain.AccountType

data class CreateAccountDTO(val ownerId: Long, val type: AccountType)

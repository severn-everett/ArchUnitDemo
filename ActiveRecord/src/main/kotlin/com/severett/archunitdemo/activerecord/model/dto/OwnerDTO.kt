package com.severett.archunitdemo.activerecord.model.dto

data class OwnerDTO(val id: Long, val name: String, val surname: String, val accounts: List<Long>)

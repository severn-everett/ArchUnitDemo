package com.severett.archunitdemo.legacy.controller

data class OwnerDTO(val id: Long, val name: String, val surname: String, val accounts: List<Long>)

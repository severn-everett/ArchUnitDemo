package com.severett.archunitdemo.activerecord.model.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity
class Account(
    @field:Id
    var id: Long = 0L,
    var balance: BigDecimal = BigDecimal.ZERO,
    var type: AccountType,
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: Owner
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

package com.severett.archunitdemo.activerecord.model.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity
class BankAccount(
    @field:Id
    var id: Long,
    var amount: BigDecimal,
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: Owner
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BankAccount) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

package com.severett.archunitdemo.legacy.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity
class Account(
    @field:Id
    var id: Long = 0L,
    var balance: BigDecimal = BigDecimal.ZERO,
    @field:Enumerated(EnumType.STRING)
    var type: AccountType,
    @field:ManyToOne
    @field:JoinColumn(name = "owner_id", nullable = false)
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

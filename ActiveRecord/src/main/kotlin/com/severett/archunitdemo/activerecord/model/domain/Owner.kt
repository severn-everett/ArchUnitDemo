package com.severett.archunitdemo.activerecord.model.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Owner(
    @field:Id
    var id: Long = 0,
    var name: String,
    var surname: String,
    @OneToMany(mappedBy = "owner")
    var accounts: Set<Account> = emptySet(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Owner) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

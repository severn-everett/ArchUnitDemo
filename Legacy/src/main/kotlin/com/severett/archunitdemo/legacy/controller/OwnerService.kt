package com.severett.archunitdemo.legacy.controller

import com.severett.archunitdemo.legacy.model.CreateModifyOwnerDTO
import com.severett.archunitdemo.legacy.model.Owner
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class OwnerService(private val ownerDBAccess: OwnerDBAccess) {
    @Transactional
    fun createOwner(ownerDTO: CreateModifyOwnerDTO) {
        ownerDBAccess.save(Owner(name = ownerDTO.name, surname = ownerDTO.surname))
    }

    fun getOwner(id: Long) = ownerDBAccess.findByIdOrNull(id)
        ?.let { owner ->
            OwnerDTO(
                id = owner.id,
                name = owner.name,
                surname = owner.surname,
                accounts = owner.accounts.map { it.id }
            )
        } ?: throw EntityNotFoundException("No owner found for id $id")

    @Transactional
    fun updateOwner(id: Long, ownerDTO: CreateModifyOwnerDTO) {
        val owner = ownerDBAccess.getReferenceById(id)
        owner.name = ownerDTO.name
        owner.surname = ownerDTO.surname
        ownerDBAccess.save(owner)
    }
}

@Repository
interface OwnerDBAccess : JpaRepository<Owner, Long>

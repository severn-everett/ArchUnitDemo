package com.severett.archunitdemo.activerecord.service

import com.severett.archunitdemo.activerecord.model.domain.Owner
import com.severett.archunitdemo.activerecord.model.dto.CreateModifyOwnerDTO
import com.severett.archunitdemo.activerecord.model.dto.OwnerDTO
import com.severett.archunitdemo.activerecord.repo.OwnerRepo
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OwnerService(private val ownerRepo: OwnerRepo) {
    @Transactional
    fun createOwner(ownerDTO: CreateModifyOwnerDTO) {
        ownerRepo.save(Owner(name = ownerDTO.name, surname = ownerDTO.surname))
    }

    fun getOwner(id: Long) = ownerRepo.findByIdOrNull(id)
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
        val owner = ownerRepo.getReferenceById(id)
        owner.name = ownerDTO.name
        owner.surname = ownerDTO.surname
        ownerRepo.save(owner)
    }

    @Transactional
    fun deleteOwner(id: Long) {
        ownerRepo.deleteById(id)
    }
}

package com.severett.archunitdemo.activerecord.controller

import com.severett.archunitdemo.activerecord.model.dto.CreateModifyOwnerDTO
import com.severett.archunitdemo.activerecord.model.dto.OwnerDTO
import com.severett.archunitdemo.activerecord.service.OwnerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/owners")
class OwnerController(private val ownerService: OwnerService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOwner(@RequestBody ownerDTO: CreateModifyOwnerDTO) {
        ownerService.createOwner(ownerDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getOwner(@PathVariable id: Long): OwnerDTO {
        val owner = ownerService.getOwner(id)
        return OwnerDTO(
            id = owner.id,
            name = owner.name,
            surname = owner.surname,
            accounts = owner.accounts.map { it.id }
        )
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateOwner(@PathVariable id: Long, @RequestBody ownerDTO: CreateModifyOwnerDTO) {
        ownerService.updateOwner(id, ownerDTO)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteOwner(@PathVariable id: Long) {
        ownerService.deleteOwner(id)
    }
}

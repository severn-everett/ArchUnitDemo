package com.severett.archunitdemo.legacy.service

import com.severett.archunitdemo.legacy.controller.OwnerDBAccess
import com.severett.archunitdemo.legacy.controller.OwnerService
import com.severett.archunitdemo.legacy.model.CreateModifyOwnerDTO
import jakarta.transaction.Transactional
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
class OwnerController(private val ownerService: OwnerService, private val ownerDBAccess: OwnerDBAccess) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOwner(@RequestBody ownerDTO: CreateModifyOwnerDTO) {
        ownerService.createOwner(ownerDTO)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun getOwner(@PathVariable id: Long) = ownerService.getOwner(id)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateOwner(@PathVariable id: Long, @RequestBody ownerDTO: CreateModifyOwnerDTO) {
        ownerService.updateOwner(id, ownerDTO)
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteOwner(@PathVariable id: Long) {
        ownerDBAccess.deleteById(id)
    }
}

package com.acpdq.credit.app.system.controllers

import com.acpdq.credit.app.system.dtos.CustomerDTO
import com.acpdq.credit.app.system.dtos.CustomerUpdateDTO
import com.acpdq.credit.app.system.dtos.CustomerViewDTO
import com.acpdq.credit.app.system.entities.Customer
import com.acpdq.credit.app.system.services.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDTO): ResponseEntity<CustomerViewDTO> {
        val savedCustomer = customerService.save(customerDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body( CustomerViewDTO(savedCustomer) )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<CustomerViewDTO> {
        val customer: Customer = customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerViewDTO(customer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long) = customerService.delete(id)

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") id: Long,
                       @RequestBody @Valid customerUpdateDTO: CustomerUpdateDTO): ResponseEntity<CustomerViewDTO> {
        var customer: Customer = customerService.findById(id)
        customer = customerUpdateDTO.toEntity(customer)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerViewDTO( customerService.save(customer)))
    }
}
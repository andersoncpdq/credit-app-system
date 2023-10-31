package com.acpdq.credit.app.system.dtos

import com.acpdq.credit.app.system.entities.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDTO(
    @field:NotEmpty(message = "Entrada inválida!")
    val firstName: String,

    @field:NotEmpty(message = "Entrada inválida!")
    val lastName: String,

    @field:NotNull(message = "Entrada inválida!")
    val income: BigDecimal,

    @field:NotEmpty(message = "Entrada inválida!")
    val zipcode: String,

    @field:NotEmpty(message = "Entrada inválida!")
    val street: String
) {

    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipcode
        customer.address.street = this.street
        return customer
    }
}

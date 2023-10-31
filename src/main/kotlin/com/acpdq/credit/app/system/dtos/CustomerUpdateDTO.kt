package com.acpdq.credit.app.system.dtos

import com.acpdq.credit.app.system.entities.Customer
import java.math.BigDecimal

data class CustomerUpdateDTO(
    val firstName: String,
    val lastName: String,
    val income: BigDecimal,
    val zipcode: String,
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

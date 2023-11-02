package com.acpdq.credit.app.system.dtos

import com.acpdq.credit.app.system.entities.Address
import com.acpdq.credit.app.system.entities.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
    @field:NotEmpty(message = "Entrada inválida!")
    val firstName: String,

    @field:NotEmpty(message = "Entrada inválida!")
    val lastName: String,

    @field:CPF(message = "CPF inválido!")
    val cpf: String,

    @field:NotNull(message = "Entrada inválida!")
    val income: BigDecimal,

    @field:NotEmpty(message = "Entrada inválida!")
    val email: String,

    @field:NotEmpty(message = "Entrada inválida!")
    val password: String,

    @field:NotEmpty(message = "Entrada inválida!")
    val zipCode: String,

    @field:NotEmpty(message = "Entrada inválida!")
    val street: String
) {

    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}

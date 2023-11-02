package com.acpdq.credit.app.system.dtos

import com.acpdq.credit.app.system.entities.Credit
import com.acpdq.credit.app.system.entities.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    @field:Positive(message = "Precisa ser maior que zero!")
    val creditValue: BigDecimal,

    @field:Future(message = "O dia precisa ser no futuro!")
    val dayFirstOfInstallment: LocalDate,

    @field:Min(value = 1)
    @field:Max(value = 48)
    val numberOfInstallments: Int,

    @field:NotNull(message = "Entrada inválida!")
    val customerId: Long
) {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}

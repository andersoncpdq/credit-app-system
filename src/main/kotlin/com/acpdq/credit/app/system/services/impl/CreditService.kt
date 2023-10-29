package com.acpdq.credit.app.system.services.impl

import com.acpdq.credit.app.system.entities.Credit
import com.acpdq.credit.app.system.repositories.CreditRepository
import com.acpdq.credit.app.system.services.ICreditService
import java.lang.RuntimeException
import java.util.*

class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomerId(customerId: Long): List<Credit> {
        return this.creditRepository.findAllByCustomerId(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = this.creditRepository.findByCreditCode(creditCode)
            ?: throw RuntimeException("CreditCode $creditCode not found")

        return if (credit.customer?.id == customerId) credit
                else throw RuntimeException("Invalid operation")
    }
}
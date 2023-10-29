package com.acpdq.credit.app.system.services.impl

import com.acpdq.credit.app.system.entities.Customer
import com.acpdq.credit.app.system.repositories.CustomerRepository
import com.acpdq.credit.app.system.services.ICustomerService
import java.lang.RuntimeException

class CustomerService(
    private val customerRepository: CustomerRepository
): ICustomerService {
    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(id: Long): Customer = this.customerRepository.findById(id).orElseThrow {
        throw RuntimeException("Id $id not found")
    }

    override fun delete(id: Long) {
        if ( !customerRepository.existsById(id) )
            throw RuntimeException("Id $id not found")

        this.customerRepository.deleteById(id)
    }
}
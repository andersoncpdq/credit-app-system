package com.acpdq.credit.app.system.services

import com.acpdq.credit.app.system.entities.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long)
}
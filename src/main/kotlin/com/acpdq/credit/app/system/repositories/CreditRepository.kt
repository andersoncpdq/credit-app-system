package com.acpdq.credit.app.system.repositories

import com.acpdq.credit.app.system.entities.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository: JpaRepository<Credit, Long> {
}
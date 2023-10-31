package com.acpdq.credit.app.system.controllers

import com.acpdq.credit.app.system.dtos.CreditDTO
import com.acpdq.credit.app.system.dtos.CreditViewDTO
import com.acpdq.credit.app.system.entities.Credit
import com.acpdq.credit.app.system.services.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/credits")
class CreditController(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDTO): ResponseEntity<String> {
        val credit = creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body("Credit ${credit.creditCode} - " +
                                     "Customer ${credit.customer?.firstName} saved!")
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewDTO>> {
        return ResponseEntity.status(HttpStatus.OK).body(this.creditService.findAllByCustomerId(customerId).stream()
                                                            .map { credit: Credit -> CreditViewDTO(credit) }.toList())
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditViewDTO> {
        val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(CreditViewDTO(credit))
    }
}
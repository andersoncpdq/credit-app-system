package com.acpdq.credit.app.system.services

import com.acpdq.credit.app.system.entities.Address
import com.acpdq.credit.app.system.entities.Customer
import com.acpdq.credit.app.system.exceptions.BusinessException
import com.acpdq.credit.app.system.repositories.CustomerRepository
import com.acpdq.credit.app.system.services.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK lateinit var customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun `should create customer`() {
        // given
        val fakeCustomer: Customer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer
        // when
        val actual: Customer = customerService.save(fakeCustomer)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify (exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should find customer by id`() {
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)

        val actual: Customer = customerService.findById(fakeId)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `find by id should throw BusinessException when invalid id`() {
        val fakeId: Long = Random().nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findById(fakeId) }
            .withMessage("Id $fakeId not found")
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should delete customer by id`() {
        val fakeId: Long = Random().nextLong()
        every { customerRepository.deleteById(fakeId) } just runs
        every { customerRepository.existsById(fakeId) } returns true

        customerService.delete(fakeId)

        verify(exactly = 1) { customerRepository.existsById(fakeId) }
        verify(exactly = 1) { customerRepository.deleteById(fakeId) }
    }

    @Test
    fun `delete should throw BusinessException when invalid id`() {
        val fakeId: Long = Random().nextLong()
        every { customerRepository.existsById(fakeId) } returns false

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.delete(fakeId) }
            .withMessage("Id $fakeId not found")

        verify(exactly = 1) { customerRepository.existsById(fakeId) }
        verify(exactly = 0) { customerRepository.deleteById(fakeId) }
    }

    fun buildCustomer(
        firstName: String = "Anderson",
        lastName: String = "Couto",
        cpf: String = "28475934625",
        email: String = "acpdq@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua tal",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )
}
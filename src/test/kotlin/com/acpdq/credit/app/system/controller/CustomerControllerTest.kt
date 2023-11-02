package com.acpdq.credit.app.system.controller

import com.acpdq.credit.app.system.dtos.CustomerDTO
import com.acpdq.credit.app.system.dtos.CustomerUpdateDTO
import com.acpdq.credit.app.system.entities.Customer
import com.acpdq.credit.app.system.repositories.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControllerTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return 201 status`() {
        val customerDto: CustomerDTO = builderCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated)
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Anderson"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Couto"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("acpdq@email.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("1000.0"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("000000"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua tal"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
    }

    @Test
    fun `should not save a customer with same CPF and return 409 status`() {
        customerRepository.save(builderCustomerDto().toEntity())

        val customerDto: CustomerDTO = builderCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
        .andExpect(MockMvcResultMatchers.status().isConflict)
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict!"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.exception")
                .value("class org.springframework.dao.DataIntegrityViolationException")
        )
        .andExpect(MockMvcResultMatchers.jsonPath("$.messages[*]").isNotEmpty)
    }

    @Test
    fun `should not save a customer with empty firstName and return 400 status`() {
        val customerDto: CustomerDTO = builderCustomerDto(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .content(valueAsString)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.exception")
                .value("class org.springframework.web.bind.MethodArgumentNotValidException")
        )
        .andExpect(MockMvcResultMatchers.jsonPath("$.messages[*]").isNotEmpty)
    }

    @Test
    fun `should find customer by id and return 200 status`() {
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Anderson"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Couto"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("acpdq@email.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("1000.0"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("000000"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua tal"))
    }

    @Test
    fun `should not find customer with invalid id and return 400 status`() {
        val invalidId: Long = 2L

        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$invalidId")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class com.acpdq.credit.app.system.exceptions.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.messages[*]").isNotEmpty)
    }

    @Test
    fun `should delete customer by id and return 204 status`() {
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())

        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should not delete customer by id and return 400 status`() {
        val invalidId: Long = 2L

        mockMvc.perform(
            MockMvcRequestBuilders.delete("$URL/${invalidId}")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.exception")
                .value("class com.acpdq.credit.app.system.exceptions.BusinessException")
        )
        .andExpect(MockMvcResultMatchers.jsonPath("$.messages[*]").isNotEmpty)
    }

    @Test
    fun `should update a customer and return 200 status`() {
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val customerUpdateDto: CustomerUpdateDTO = builderCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("AndersonUpdate"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("CoutoUpdate"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("28475934625"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("acpdq@email.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("5000.0"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("45656"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua Updated"))
    }

    @Test
    fun `should not update a customer with invalid id and return 400 status`() {
        val invalidId: Long = 2L
        val customerUpdateDto: CustomerUpdateDTO = builderCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=$invalidId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest)
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.exception")
                .value("class com.acpdq.credit.app.system.exceptions.BusinessException")
        )
        .andExpect(MockMvcResultMatchers.jsonPath("$.messages[*]").isNotEmpty)
    }

    private fun builderCustomerDto(
        firstName: String = "Anderson",
        lastName: String = "Couto",
        cpf: String = "28475934625",
        email: String = "acpdq@email.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "1234",
        zipCode: String = "000000",
        street: String = "Rua tal",
    ) = CustomerDTO(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )

    private fun builderCustomerUpdateDto(
        firstName: String = "AndersonUpdate",
        lastName: String = "CoutoUpdate",
        income: BigDecimal = BigDecimal.valueOf(5000.0),
        zipCode: String = "45656",
        street: String = "Rua Updated"
    ): CustomerUpdateDTO = CustomerUpdateDTO(
        firstName = firstName,
        lastName = lastName,
        income = income,
        zipCode = zipCode,
        street = street
    )
}
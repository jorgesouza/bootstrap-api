package com.example.bootstrap.api.domain.dtos

import com.example.bootstrap.api.infra.persistence.entities.Accounting
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AccountingDTO(
        var id: Long,

        @field:NotNull @field:NotEmpty
        var description: String,

        @field:NotNull
        var paymentDate: LocalDate,

        @field:NotNull
        var dueDate: LocalDate,

        @field:NotNull
        var value: BigDecimal,

        @field:NotNull
        var category: CategoryDTO,

        var updatedAt: LocalDateTime = LocalDateTime.now(),

        var createdAt: LocalDateTime = LocalDateTime.now()
)

// Extension Functions
fun AccountingDTO.toEntity() = Accounting(
        id = id,
        description = description,
        paymentDate = paymentDate,
        dueDate = dueDate,
        value = value,
        category = category.toEntity(),
        updatedAt = updatedAt,
        createdAt = createdAt
)

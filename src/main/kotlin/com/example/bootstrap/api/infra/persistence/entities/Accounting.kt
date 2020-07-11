package com.example.bootstrap.api.infra.persistence.entities

import com.example.bootstrap.api.domain.dtos.AccountingDTO
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "accountings")
data class Accounting(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        var description: String,

        var paymentDate: LocalDate,

        var dueDate: LocalDate,

        var value: BigDecimal,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        var category: Category,

        var updatedAt: LocalDateTime,

        var createdAt: LocalDateTime = LocalDateTime.now()
)

// Extension Functions
fun Accounting.toDTO() = AccountingDTO(
        id = id,
        description = description,
        paymentDate = paymentDate,
        dueDate = dueDate,
        value = value,
        category = category.toDTO(),
        updatedAt = updatedAt,
        createdAt = createdAt
)

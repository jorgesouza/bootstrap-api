package com.example.bootstrap.api.infra.persistence.entities

import com.example.bootstrap.api.domain.dtos.CategoryDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "categories")
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        var name: String,

        var createdAt: LocalDateTime = LocalDateTime.now()
)

// Extension Functions
fun Category.toDTO() = CategoryDTO(
        id = id,
        name = name,
        createdAt = createdAt
)

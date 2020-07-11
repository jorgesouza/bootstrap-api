package com.example.bootstrap.api.infra.persistence.repositories

import com.example.bootstrap.api.infra.persistence.entities.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {

    fun findByNameIgnoreCase(name: String?, pageable: Pageable): Page<Category>
}

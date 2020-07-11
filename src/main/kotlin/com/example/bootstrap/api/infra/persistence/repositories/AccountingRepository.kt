package com.example.bootstrap.api.infra.persistence.repositories

import com.example.bootstrap.api.infra.persistence.entities.Accounting
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountingRepository : JpaRepository<Accounting, Long> {

    fun findByDescriptionContainingIgnoreCase(description: String?, pageable: Pageable): Page<Accounting>
}

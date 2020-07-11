package com.example.bootstrap.api.domain.services

import com.example.bootstrap.api.domain.dtos.AccountingDTO
import com.example.bootstrap.api.domain.dtos.toEntity
import com.example.bootstrap.api.infra.exceptions.CategoryDoesNotExistException
import com.example.bootstrap.api.infra.persistence.entities.Accounting
import com.example.bootstrap.api.infra.persistence.entities.toDTO
import com.example.bootstrap.api.infra.persistence.repositories.AccountingRepository
import org.springframework.beans.BeanUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class AccountingService(private val accountingRepository: AccountingRepository,
                        private val categoryService: CategoryService) {

    fun save(accountingDTO: AccountingDTO): AccountingDTO
            = accountingRepository.save(accountingDTO.toEntity()).toDTO()

    fun findAll(description: String?, pageable: Pageable): Page<AccountingDTO> {
        if (!description.isNullOrEmpty())
            return accountingRepository.findByDescriptionContainingIgnoreCase(description, pageable).map { it.toDTO() }

        return accountingRepository.findAll(pageable).map { it.toDTO() }
    }

    fun findById(id: Long): Optional<Accounting>
            = accountingRepository.findById(id)

    fun update(id: Long, accountingDTO: AccountingDTO): AccountingDTO {
        val accounting = accountingRepository.findByIdOrNull(id) ?: throw EmptyResultDataAccessException(1)

        BeanUtils.copyProperties(accountingDTO, accounting, "id", "createdAt")

        val category = categoryService.findById(accountingDTO.category.id)
        if (!category.isPresent)
            throw CategoryDoesNotExistException("Category does not exist exception: ${accountingDTO.category.id}")

        accounting.category = category.get()

        return accounting.toDTO()
    }

    fun deleteById(id: Long)
            = accountingRepository.deleteById(id)
}

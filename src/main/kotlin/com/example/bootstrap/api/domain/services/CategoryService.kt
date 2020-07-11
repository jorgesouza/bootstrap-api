package com.example.bootstrap.api.domain.services

import com.example.bootstrap.api.domain.dtos.CategoryDTO
import com.example.bootstrap.api.domain.dtos.toEntity
import com.example.bootstrap.api.infra.persistence.entities.Category
import com.example.bootstrap.api.infra.persistence.entities.toDTO
import com.example.bootstrap.api.infra.persistence.repositories.CategoryRepository
import org.springframework.beans.BeanUtils
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun save(categoryDTO: CategoryDTO): CategoryDTO
            = categoryRepository.save(categoryDTO.toEntity()).toDTO()

    fun findAll(name: String?, pageable: Pageable): Page<CategoryDTO> {
        if (!name.isNullOrEmpty())
            return categoryRepository.findByNameIgnoreCase(name, pageable).map { it.toDTO() }

        return categoryRepository.findAll(pageable).map { it.toDTO() }
    }

    fun findById(id: Long): Optional<Category>
            = categoryRepository.findById(id)

    fun update(id: Long, categoryDTO: CategoryDTO): CategoryDTO {
        val category = categoryRepository.findByIdOrNull(id) ?: throw EmptyResultDataAccessException(1)

        BeanUtils.copyProperties(categoryDTO, category, "id", "createdAt")

        return category.toDTO()
    }

    fun deleteById(id: Long)
            = categoryRepository.deleteById(id)
}

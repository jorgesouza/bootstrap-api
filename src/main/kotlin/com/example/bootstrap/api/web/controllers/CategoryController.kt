package com.example.bootstrap.api.web.controllers

import com.example.bootstrap.api.domain.dtos.CategoryDTO
import com.example.bootstrap.api.domain.services.CategoryService
import com.example.bootstrap.api.infra.persistence.entities.toDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {

    @PostMapping
    fun save(@RequestBody @Valid categoryDTO: CategoryDTO, uriBuilder: UriComponentsBuilder):
            ResponseEntity<CategoryDTO> {
        val newCategoryDTO = categoryService.save(categoryDTO)

        val uri = uriBuilder.path("/categories/{id}").buildAndExpand(newCategoryDTO.id).toUri()

        return ResponseEntity.created(uri).body(newCategoryDTO)
    }

    // Consulta com filtro, paginação e ordenação
    @GetMapping
    fun findAll(@RequestParam(required = false) name: String?,
                @PageableDefault(sort = ["id"],
                        direction = Sort.Direction.DESC,
                        size = 10,
                        page = 0) pageable: Pageable): ResponseEntity<Collection<CategoryDTO>> {

        return ResponseEntity.ok(categoryService.findAll(name, pageable).content)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CategoryDTO> {
        val category = categoryService.findById(id)

        if (!category.isPresent)
            return ResponseEntity.notFound().build()

        return ResponseEntity.ok(category.get().toDTO())
    }

    @PutMapping("/{id}")
    @Transactional
    fun update(@PathVariable id: Long, @RequestBody @Valid categoryDTO: CategoryDTO): ResponseEntity<CategoryDTO> {
        return ResponseEntity.ok(categoryService.update(id, categoryDTO))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Long) = categoryService.deleteById(id)
}

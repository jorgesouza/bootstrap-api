package com.example.bootstrap.api.web.controllers

import com.example.bootstrap.api.domain.dtos.AccountingDTO
import com.example.bootstrap.api.domain.services.AccountingService
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
@RequestMapping("/accountings")
class AccountingController(private val accountingService: AccountingService) {

    @PostMapping
    fun save(@RequestBody @Valid accountingDTO: AccountingDTO, uriBuilder: UriComponentsBuilder):
            ResponseEntity<AccountingDTO> {
        val newAccountingDTO = accountingService.save(accountingDTO)

        val uri = uriBuilder.path("/accountings/{id}").buildAndExpand(newAccountingDTO.id).toUri()

        return ResponseEntity.created(uri).body(newAccountingDTO)
    }

    // Consulta com filtro, paginação e ordenação
    @GetMapping
    fun findAll(@RequestParam(required = false) description: String?,
                @PageableDefault(sort = ["id"],
                        direction = Sort.Direction.DESC,
                        size = 10,
                        page = 0) pageable: Pageable): ResponseEntity<Collection<AccountingDTO>> {

        return ResponseEntity.ok(accountingService.findAll(description, pageable).content)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<AccountingDTO> {
        val accounting = accountingService.findById(id)

        if (!accounting.isPresent)
            return ResponseEntity.notFound().build()

        return ResponseEntity.ok(accounting.get().toDTO())
    }

    @PutMapping("/{id}")
    @Transactional
    fun update(@PathVariable id: Long, @RequestBody @Valid accountingDTO: AccountingDTO): ResponseEntity<AccountingDTO> {
        return ResponseEntity.ok(accountingService.update(id, accountingDTO))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Long) = accountingService.deleteById(id)
}

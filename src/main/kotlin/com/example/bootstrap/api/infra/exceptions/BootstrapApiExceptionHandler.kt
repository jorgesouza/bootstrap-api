package com.example.bootstrap.api.infra.exceptions

import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class BootstrapApiExceptionHandler(private val messageSource: MessageSource) : ResponseEntityExceptionHandler() {

    // Valida atributos desconhecidos inseridos no JSON
    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val message = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale())
        val exception = ex.message.toString()
        val errors = listOf(Error(message, exception))

        return handleExceptionInternal(ex, errors, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    // Para auxiliar no retorno das validações pelo Bean Validation
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val errors = getErrors(ex.bindingResult)

        return handleExceptionInternal(ex, errors, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(CategoryDoesNotExistException::class)
    fun handleCategoryDoesNotExistException(ex: CategoryDoesNotExistException, request: WebRequest): ResponseEntity<Any> {
        val message = messageSource.getMessage("resource.operation-not-allowed", null, LocaleContextHolder.getLocale())
        val exception = ex.toString()
        val error = Error(message, exception)

        return handleExceptionInternal(ex, error, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    // Quando tentar realizar update em um recurso não existente
    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultDataAccessException(ex: EmptyResultDataAccessException, request: WebRequest): ResponseEntity<Any> {
        val message = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale())
        val exception = ex.toString()
        val error = Error(message, exception)

        return handleExceptionInternal(ex, error, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    // Valida quando um usuário informar uma categoria inexistente no cadastro de uma nova Accounting, por exemplo
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException, request: WebRequest): ResponseEntity<Any> {
        val message = messageSource.getMessage("resource.operation-not-allowed", null, LocaleContextHolder.getLocale())
        val exception = ExceptionUtils.getRootCauseMessage(ex) // org.apache.commons:commons-lang3:3.9 - Uma mensagem mais descritiva do erro
        val error = Error(message, exception)

        return handleExceptionInternal(ex, error, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    private fun getErrors(bindingResult: BindingResult): Collection<Error> {
        val erros = ArrayList<Error>()

        bindingResult.fieldErrors.forEach { fieldError ->
            val message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())
            val exception = fieldError.toString()
            erros.add(Error(message, exception))
        }

        return erros
    }

    inner class Error(val message: String, val exception: String)
}

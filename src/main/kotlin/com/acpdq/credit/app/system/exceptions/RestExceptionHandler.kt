package com.acpdq.credit.app.system.exceptions

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionsDetails> {
        val errors: MutableMap<String, String?> = HashMap()

        ex.bindingResult.allErrors.stream().forEach {
            error: ObjectError ->
            val fieldName: String = (error as FieldError).field
            val messageError: String? = error.defaultMessage
            errors[fieldName] = messageError
        }

        return ResponseEntity(
            ExceptionsDetails(
                title = "Bad Request",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                messages = errors
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handlerDataAccessException(ex: DataAccessException): ResponseEntity<ExceptionsDetails> {
        return ResponseEntity(
            ExceptionsDetails(
                title = "Conflict!",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                exception = ex.javaClass.toString(),
                messages = mutableMapOf(ex.cause.toString() to ex.message)
            ), HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handlerBusinessException(ex: BusinessException): ResponseEntity<ExceptionsDetails> {
        return ResponseEntity(
            ExceptionsDetails(
                title = "Bad Request!",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                messages = mutableMapOf(ex.cause.toString() to ex.message)
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlerIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ExceptionsDetails> {
        return ResponseEntity(
            ExceptionsDetails(
                title = "Bad Request!",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.toString(),
                messages = mutableMapOf(ex.cause.toString() to ex.message)
            ), HttpStatus.BAD_REQUEST
        )
    }
}
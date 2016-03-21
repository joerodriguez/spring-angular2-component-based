package com.github.joerodriguez.sbng2ex.web

import com.github.joerodriguez.sbng2ex.service.ServiceError
import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class Handler {

    @ExceptionHandler(ServiceFailureError::class)
    fun handleServiceFailureErrors(ex: ServiceFailureError): ResponseEntity<Errors> {
        return ResponseEntity(Errors(ex.errors), ex.failureStatusCode)
    }

    data class Errors(val errors: List<ServiceError>)
}

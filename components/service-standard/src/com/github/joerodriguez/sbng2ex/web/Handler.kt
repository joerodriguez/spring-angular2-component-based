package com.github.joerodriguez.sbng2ex.web

import com.github.joerodriguez.sbng2ex.service.ServiceError
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class Handler {

    @ExceptionHandler(ServiceFailureError::class)
    fun handleServiceFailureErrors(ex: ServiceFailureError): HttpEntity<Errors> {
        return ResponseEntity(Errors(ex.errors), ex.failureStatusCode)
    }

    data class Errors(val errors: List<ServiceError>)
}

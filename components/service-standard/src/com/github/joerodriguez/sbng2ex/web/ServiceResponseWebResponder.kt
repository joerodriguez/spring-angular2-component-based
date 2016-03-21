package com.github.joerodriguez.sbng2ex.web

import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ServiceResponseWebResponder {

    fun <T> respond(
            response: ServiceResponse<T>,
            succcessStatusCode: HttpStatus,
            failureStatusCode: HttpStatus
    ) : ResponseEntity<T> {
        if (!response.isSuccess) {
            throw ServiceFailureError(response.getErrors(), failureStatusCode)
        }

        return ResponseEntity<T>(response.entity, succcessStatusCode)
    }
}

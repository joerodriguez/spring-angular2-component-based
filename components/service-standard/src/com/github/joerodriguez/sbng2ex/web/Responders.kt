package com.github.joerodriguez.sbng2ex.web

import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
open class Responders {

    fun <T> post(response: ServiceResponse<T>): HttpEntity<T> {
        return putOrPost(response, HttpStatus.CREATED)
    }

    fun <T> put(response: ServiceResponse<T>): HttpEntity<T> {
        return putOrPost(response, HttpStatus.OK)
    }

    private fun <T> putOrPost(
            response: ServiceResponse<T>,
            status: HttpStatus
    ): HttpEntity<T> {

        if (!response.isSuccess) {
            throw ServiceFailureError(response.errors, HttpStatus.UNPROCESSABLE_ENTITY)
        }

        if (response.entity == null) {
            return ResponseEntity(status)
        }

        return ResponseEntity(response.entity, status)
    }
}

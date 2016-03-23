package com.github.joerodriguez.sbng2ex.web

import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.UriComponentsBuilder

@Component
open class Responders {

    fun <T> post(response: ServiceResponse<T>, uriPath: ((T) -> String)?): HttpEntity<Void> {
        return putOrPost(response, uriPath, HttpStatus.CREATED)
    }

    fun <T> put(response: ServiceResponse<T>, uriPath: ((T) -> String)?): HttpEntity<Void> {
        return putOrPost(response, uriPath, HttpStatus.NO_CONTENT)
    }

    private fun <T> putOrPost(
            response: ServiceResponse<T>,
            uriPath: ((T) -> String)?,
            status: HttpStatus
    ): HttpEntity<Void> {

        if (!response.isSuccess) {
            throw ServiceFailureError(response.errors, HttpStatus.UNPROCESSABLE_ENTITY)
        }

        if (response.entity != null && uriPath != null) {
            val path: String = uriPath.invoke(response.entity)
            val requestAttributes = RequestContextHolder.getRequestAttributes();

            if (requestAttributes is ServletRequestAttributes) {

                val location = UriComponentsBuilder
                        .fromUriString(requestAttributes.request.requestURL.toString())
                        .replacePath(path)
                        .replaceQuery("")
                        .build()
                        .toUri()

                return ResponseEntity.status(status).location(location).build()
            }
        }

        return ResponseEntity(status)
    }
}

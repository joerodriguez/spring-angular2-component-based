package com.github.joerodriguez.sbng2ex.web

import com.github.joerodriguez.sbng2ex.service.ServiceError
import org.springframework.http.HttpStatus

class ServiceFailureError(val errors: List<ServiceError>, val failureStatusCode: HttpStatus) : RuntimeException()

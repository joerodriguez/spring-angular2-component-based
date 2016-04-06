package com.github.joerodriguez.sbng2ex.servicestandard.web

import com.github.joerodriguez.sbng2ex.servicestandard.service.ServiceError
import org.springframework.http.HttpStatus

class ServiceFailureError(val errors: List<ServiceError>, val failureStatusCode: HttpStatus) : RuntimeException()

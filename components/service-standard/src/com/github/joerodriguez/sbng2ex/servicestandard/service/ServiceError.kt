package com.github.joerodriguez.sbng2ex.servicestandard.service

class ServiceError private constructor(
        val code: String,
        val message: String,
        val fieldName: String
) {

    companion object {
        fun create(errorType: ErrorType, fieldName: String): ServiceError {
            return ServiceError(errorType.code, errorType.message, fieldName)
        }
    }
}

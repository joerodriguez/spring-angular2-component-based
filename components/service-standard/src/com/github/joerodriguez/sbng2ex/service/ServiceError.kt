package com.github.joerodriguez.sbng2ex.service

class ServiceError private constructor(
        val code: String,
        val message: String,
        val fieldName: String,
        val extendedMessage: String?
) {

    companion object {
        fun create(errorType: ErrorType, fieldName: String, extendedMessage: String? = null): ServiceError {
            return ServiceError(errorType.code, errorType.message, fieldName, extendedMessage)
        }
    }
}

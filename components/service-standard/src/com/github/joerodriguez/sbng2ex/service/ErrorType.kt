package com.github.joerodriguez.sbng2ex.service

enum class ErrorType private constructor(val code: String, val message: String) {
    SYSTEM_UNEXPECTED("unknown_error", "Unknown Error"),
    INVALID_EMAIL("email_invalid", "Email address is invalid"),
    ILLEGAL_ACCESS("illegal_access", "Illegal Access"),
    EMAIL_TAKEN("email_taken", "Email address has already been taken");


    fun forField(field: String): ServiceError {
        return ServiceError.create(this, field)
    }
}

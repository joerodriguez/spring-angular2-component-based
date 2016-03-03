package com.github.joerodriguez.sbng2ex.service;

public enum ErrorType {
    SYSTEM_UNEXPECTED("unknown_error", "Unknown Error"),
    INVALID_EMAIL("email_invalid", "Email address is invalid"),
    EMAIL_TAKEN("email_taken", "Email address has already been taken");

    private final String code;
    private final String message;

    ErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public ServiceError forField(String field) {
        return ServiceError.create(this, field);
    }
}

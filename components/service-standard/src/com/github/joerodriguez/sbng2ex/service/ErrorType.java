package com.github.joerodriguez.sbng2ex.service;

public enum ErrorType {
    UNKNOWN(-1, "Unknown Error"),
    INVALID_EMAIL(1, "Email address is invalid"),
    EMAIL_TAKEN(2, "Email address has already been taken");

    private final int code;
    private final String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public ServiceError forField(String field) {
        return ServiceError.create(this, field);
    }
}

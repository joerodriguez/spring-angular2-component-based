package com.github.joerodriguez.sbng2ex.service;

public class ServiceError {
    private final int code;
    private final String message;
    private final String fieldName;
    private final String extendedMessage;

    public static ServiceError create(ErrorType errorType, String fieldName) {
        return create(errorType, fieldName, null);
    }

    public static ServiceError create(ErrorType errorType, String fieldName, String extendedMessage) {
        return new ServiceError(errorType.getCode(), errorType.getMessage(), fieldName, extendedMessage);
    }

    private ServiceError(int code, String message, String fieldName, String extendedMessage) {
        this.code = code;
        this.message = message;
        this.fieldName = fieldName;
        this.extendedMessage = extendedMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }

    public String getFieldName() {
        return fieldName;
    }
}

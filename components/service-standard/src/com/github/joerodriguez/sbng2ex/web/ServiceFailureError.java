package com.github.joerodriguez.sbng2ex.web;

import com.github.joerodriguez.sbng2ex.service.ServiceError;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ServiceFailureError extends RuntimeException {
    private final List<ServiceError> errors;
    private final HttpStatus failureStatusCode;

    public ServiceFailureError(List<ServiceError> errors, HttpStatus failureStatusCode) {
        this.errors = errors;
        this.failureStatusCode = failureStatusCode;
    }

    public List<ServiceError> getErrors() {
        return errors;
    }

    public HttpStatus getFailureStatusCode() {
        return failureStatusCode;
    }
}

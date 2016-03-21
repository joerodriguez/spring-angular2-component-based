package com.github.joerodriguez.sbng2ex.web;

import com.github.joerodriguez.sbng2ex.service.ServiceError;
import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(ServiceFailureError.class)
    public ResponseEntity<Errors> handleServiceFailureErrors(
            ServiceFailureError ex
    ) {
        return new ResponseEntity<>(new Errors(ex.getErrors()), ex.getFailureStatusCode());
    }

    public static class Errors {
        private List<ServiceError> errors;

        Errors(List<ServiceError> errors) {
            this.errors = errors;
        }

        public List<ServiceError> getErrors() {
            return errors;
        }
    }
}

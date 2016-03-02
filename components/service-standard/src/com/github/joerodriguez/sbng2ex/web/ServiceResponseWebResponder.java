package com.github.joerodriguez.sbng2ex.web;

import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServiceResponseWebResponder {
    public static <T> ResponseEntity<T> respond(
            ServiceResponse<T> response,
            HttpStatus succcessStatusCode,
            HttpStatus failureStatusCode
    ) {
        if (!response.isSuccess()) {
            throw new ServiceFailureError(response.getErrors(), failureStatusCode);
        }

        return new ResponseEntity<>(response.getEntity(), succcessStatusCode);
    }
}

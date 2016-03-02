package com.github.joerodriguez.sbng2ex.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServiceResponse<T> {
    private List<ServiceError> errors;
    private T entity;

    public ServiceResponse() {
        this.errors = new ArrayList<>();
        this.entity = null;
    }

    public static <T> ServiceResponse<T> create(Consumer<ServiceResponse<T>> consumer) {
        ServiceResponse<T> response = new ServiceResponse<>();

        consumer.accept(response);

        return response;
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public T getEntity() {
        return entity;
    }

    public List<ServiceError> getErrors() {
        return errors;
    }

    public void addError(ServiceError error) {
        this.errors.add(error);
    }

    public void commit(Supplier<T> supplier) {
        if (this.errors.isEmpty()) {
            this.entity = supplier.get();
        }
    }

    public static <T> ServiceResponse success(T entity) {
        return ServiceResponse.create((r) -> {
            r.commit(() -> entity);
        });
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

}

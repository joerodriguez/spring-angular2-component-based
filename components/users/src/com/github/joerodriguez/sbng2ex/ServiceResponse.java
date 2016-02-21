package com.github.joerodriguez.sbng2ex;

public class ServiceResponse<T> {
    public static <T> ServiceResponse<T> success(T entity) {
        return new ServiceResponse<T>(true, entity);
    }

    private boolean success;
    private T entity;

    public ServiceResponse(boolean success, T entity) {
        this.success = success;
        this.entity = entity;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getEntity() {
        return entity;
    }
}

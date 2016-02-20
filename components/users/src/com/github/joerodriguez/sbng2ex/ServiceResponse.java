package com.github.joerodriguez.sbng2ex;

public class ServiceResponse<T> {
    public static ServiceResponse success() {
        return new ServiceResponse(true);
    }

    private boolean success;

    public ServiceResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}

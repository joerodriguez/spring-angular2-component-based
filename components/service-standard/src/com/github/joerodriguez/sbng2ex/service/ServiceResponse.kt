package com.github.joerodriguez.sbng2ex.service

import java.util.ArrayList

class ServiceResponse<T> {
    private val errors: MutableList<ServiceError>
    var entity: T? = null

    init {
        this.errors = ArrayList<ServiceError>()
        this.entity = null
    }

    val isSuccess: Boolean
        get() = errors.isEmpty()

    fun getErrors(): List<ServiceError> {
        return errors
    }

    fun addError(error: ServiceError) {
        this.errors.add(error)
    }

    fun apply(supplier: () -> T) {
        if (this.errors.isEmpty()) {
            this.entity = supplier.invoke()
        }
    }

    companion object {

        fun <T> create(consumer: (ServiceResponse<T>) -> Unit): ServiceResponse<T> {
            val response = ServiceResponse<T>()

            consumer.invoke(response)

            return response
        }

        fun <T> success(entity: T): ServiceResponse<T> {
            return ServiceResponse.create {
                it.apply({ entity })
            }
        }
    }

}

package com.github.joerodriguez.sbng2ex.servicestandard.service

import java.util.ArrayList

data class ServiceResponse<T> (
    val entity: T?,
    val errors: MutableList<ServiceError>
){

    val isSuccess: Boolean
        get() = errors.isEmpty()

    companion object {

        fun <T> create(consumer: (ServiceResponseBuilder<T>) -> Unit): ServiceResponse<T> {
            val builder = ServiceResponseBuilder<T>()

            consumer.invoke(builder)

            return ServiceResponse(builder.entity, builder.errors)
        }

        fun <T> success(entity: T): ServiceResponse<T> {
            return create {
                it.apply({ entity })
            }
        }
    }

    class ServiceResponseBuilder<T> {

        val errors: MutableList<ServiceError> = ArrayList<ServiceError>()
        var entity: T? = null

        fun error(error: ServiceError) {
            this.errors.add(error)
        }

        fun apply(supplier: () -> T) {
            if (this.errors.isEmpty()) {
                this.entity = supplier.invoke()
            }
        }

    }

}


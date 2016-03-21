package com.github.joerodriguez.sbng2ex.transaction

import com.github.joerodriguez.sbng2ex.service.ErrorType
import com.github.joerodriguez.sbng2ex.service.ServiceError
import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionOperations

import java.util.ArrayList

import org.slf4j.LoggerFactory.getLogger

@Component
open class Transactions

    @Autowired
    constructor(private val transactionOperations: TransactionOperations)

{
    private val logger = getLogger(this.javaClass)

    fun <T> create(transactionConsumer: (ServiceTransaction<T>) -> Unit): ServiceResponse<T> {
        return transactionOperations.execute { status ->

            val response = ServiceResponse<T>()
            val transaction = ServiceTransaction<T>()

            try {
                transactionConsumer.invoke(transaction)
            } catch (e: Exception) {
                status.setRollbackOnly()
                logger.error("Exception occurred during transaction.", e)

                val systemUnexpectedError = ServiceError.create(ErrorType.SYSTEM_UNEXPECTED, "unknown")
                response.addError(systemUnexpectedError)
            }

            transaction.responses
                    .flatMap { it.getErrors() }
                    .forEach{ response.addError(it) };

            response.entity = transaction.entityResponse?.entity

            if (!response.getErrors().isEmpty()) {
                status.setRollbackOnly()
            }

            response
        }
    }

    class ServiceTransaction<T> {

        val responses: MutableList<ServiceResponse<*>> = ArrayList()

        var entityResponse: ServiceResponse<T>? = null
            private set

        fun add(response: ServiceResponse<*>) {
            this.responses.add(response)
        }

        fun addWithEntity(response: ServiceResponse<T>) {
            this.responses.add(response)
            this.entityResponse = response
        }
    }

}

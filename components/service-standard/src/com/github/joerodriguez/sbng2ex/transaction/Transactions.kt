package com.github.joerodriguez.sbng2ex.transaction

import com.github.joerodriguez.sbng2ex.service.ErrorType
import com.github.joerodriguez.sbng2ex.service.ServiceError
import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionOperations
import java.util.*

@Component
open class Transactions

    @Autowired
    constructor(private val transactionOperations: TransactionOperations)

{

    private val logger = getLogger(this.javaClass)

    fun <T> create(transactionConsumer: (ServiceTransactionResponseBuilder<T>) -> Unit): ServiceResponse<T> {
        return transactionOperations.execute { status ->

            val responseBuilder = ServiceResponse.ServiceResponseBuilder<T>()
            val transaction = ServiceTransactionResponseBuilder<T>()

            try {
                transactionConsumer.invoke(transaction)

                transaction.responses
                        .forEach{ responseBuilder.errors.addAll(it.errors) };

                responseBuilder.errors.addAll(transaction.errors)

                if (!responseBuilder.errors.isEmpty()) {
                    status.setRollbackOnly()
                }
                else {
                    responseBuilder.entity = transaction.entitySupplier?.invoke()
                }

            } catch (e: Exception) {
                status.setRollbackOnly()

                logger.error("Exception occurred during transaction.", e)

                val systemUnexpectedError = ServiceError.create(ErrorType.SYSTEM_UNEXPECTED, "unknown")
                responseBuilder.error(systemUnexpectedError)
            }

            ServiceResponse(responseBuilder.entity, responseBuilder.errors)
        }
    }

    class ServiceTransactionResponseBuilder <T> {

        var entitySupplier: (() -> T)? = null
        val responses: MutableList<ServiceResponse<*>> = ArrayList()
        val errors: MutableList<ServiceError> = ArrayList()

        fun add(response: ServiceResponse<*>) {
            this.responses.add(response)
        }

        fun error(error: ServiceError) {
            this.errors.add(error)
        }

        fun apply(supplier: () -> T) {
            this.entitySupplier = supplier
        }
    }

}

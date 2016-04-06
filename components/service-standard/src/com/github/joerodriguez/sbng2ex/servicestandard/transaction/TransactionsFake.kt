package com.github.joerodriguez.sbng2ex.servicestandard.transaction

import org.springframework.transaction.TransactionException
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.SimpleTransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionOperations

import java.util.ArrayList

class TransactionsFake

    private constructor(
            private val transactionManagerFake: TranactionOperationsFake
    ) : Transactions(transactionManagerFake)

{

    val statuses: List<TransactionStatus>
        get() = this.transactionManagerFake.getStatuses()

    fun success(): Boolean {
        return !statuses[0].isRollbackOnly
    }

    fun didRollback(): Boolean {
        return statuses[0].isRollbackOnly
    }

    private class TranactionOperationsFake : TransactionOperations {

        private val statuses = ArrayList<TransactionStatus>()

        fun getStatuses(): List<TransactionStatus> {
            return statuses
        }

        @Throws(TransactionException::class)
        override fun <T> execute(action: TransactionCallback<T>): T? {
            val status = SimpleTransactionStatus()
            statuses.add(status)

            try {
                return action.doInTransaction(status)
            } catch (e: Exception) {
                status.setRollbackOnly()
                return null
            }

        }

        fun reset() {
            statuses.clear()
        }
    }

    companion object {

        fun create(): TransactionsFake {
            return TransactionsFake(TranactionOperationsFake())
        }
    }

    fun reset() {
        transactionManagerFake.reset()
    }
}

package com.github.joerodriguez.sbng2ex.transaction;

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFake extends Transactions {

    private final TranactionOperationsFake transactionManagerFake;

    private TransactionsFake(TranactionOperationsFake transactionManager) {
        super(transactionManager);

        this.transactionManagerFake = transactionManager;
    }

    public static TransactionsFake create() {
        return new TransactionsFake(new TranactionOperationsFake());
    }

    public List<TransactionStatus> getStatuses() {
        return this.transactionManagerFake.getStatuses();
    }

    public boolean success() {
        return !getStatuses().get(0).isRollbackOnly();
    }

    public boolean didRollback() {
        return getStatuses().get(0).isRollbackOnly();
    }

    private static class TranactionOperationsFake implements TransactionOperations {

        private final List<TransactionStatus> statuses = new ArrayList<>();

        public List<TransactionStatus> getStatuses() {
            return statuses;
        }

        @Override
        public <T> T execute(TransactionCallback<T> action) throws TransactionException {
            SimpleTransactionStatus status = new SimpleTransactionStatus();
            statuses.add(status);

            try {
                return action.doInTransaction(status);
            } catch (Exception e) {
                status.setRollbackOnly();
                return null;
            }
        }
    }
}

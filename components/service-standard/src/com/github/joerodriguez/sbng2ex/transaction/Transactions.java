package com.github.joerodriguez.sbng2ex.transaction;

import com.github.joerodriguez.sbng2ex.service.ErrorType;
import com.github.joerodriguez.sbng2ex.service.ServiceError;
import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class Transactions {

    protected final TransactionOperations transactionOperations;

    @Autowired
    public Transactions(TransactionOperations transactionOperations) {
        this.transactionOperations = transactionOperations;
    }

    public <T> ServiceResponse<T> create(Consumer<ServiceTransaction<T>> transactionConsumer) {
        return transactionOperations.execute(status -> {
            ServiceResponse<T> response = new ServiceResponse<>();

            ServiceTransaction<T> transaction = new ServiceTransaction<>();

            try {
                transactionConsumer.accept(transaction);
            } catch (Exception e) {
                status.setRollbackOnly();
                e.printStackTrace();

                ServiceError unknown = ServiceError.create(ErrorType.UNKNOWN, "unknown", e.getMessage());
                response.addError(unknown);
            }

            transaction.responses.forEach((r) ->
                    response.getErrors().addAll(r.getErrors())
            );

            if (transaction.getEntityResponse() != null) {
                T entity = transaction.getEntityResponse().getEntity();
                response.setEntity(entity);
            }

            if (!response.getErrors().isEmpty()) {
                status.setRollbackOnly();
            }

            return response;
        });
    }

    public static class ServiceTransaction<T> {

        private final List<ServiceResponse<?>> responses;
        private ServiceResponse<T> entityResponse;

        public ServiceTransaction() {
            this.responses = new ArrayList<>();
        }

        public void add(ServiceResponse<?> response) {
            this.responses.add(response);
        }

        public void addWithEntity(ServiceResponse<T> response) {
            this.responses.add(response);
            this.entityResponse = response;
        }

        public ServiceResponse<T> getEntityResponse() {
            return entityResponse;
        }
    }

}

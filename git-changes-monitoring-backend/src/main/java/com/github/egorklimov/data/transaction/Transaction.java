package com.github.egorklimov.data.transaction;

import lombok.SneakyThrows;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.transaction.Transactional;

/**
 * Bean allowing low-level transaction management.
 */
@ApplicationScoped
public class Transaction {

    @SneakyThrows
    @Transactional
    @ActivateRequestContext
    public void execute(TransactionBlock transactionBlock) {
        transactionBlock.apply();
    }

    @SneakyThrows
    @Transactional
    @ActivateRequestContext
    public <T> T execute(CallableTransactionBlock<T> transactionBlock) {
        return transactionBlock.apply();
    }
}

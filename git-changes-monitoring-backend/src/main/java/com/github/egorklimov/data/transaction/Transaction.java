package com.github.egorklimov.data.transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.transaction.Transactional;

/**
 * Bean allowing low-level transaction management.
 */
@ApplicationScoped
public class Transaction {

    @Transactional
    @ActivateRequestContext
    public void execute(TransactionBlock transactionBlock) throws Exception {
        transactionBlock.apply();
    }

    @Transactional
    @ActivateRequestContext
    public <T> T execute(CallableTransactionBlock<T> transactionBlock) throws Exception {
        return transactionBlock.apply();
    }
}

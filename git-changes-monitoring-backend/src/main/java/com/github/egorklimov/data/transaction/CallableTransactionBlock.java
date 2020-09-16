package com.github.egorklimov.data.transaction;

@FunctionalInterface
public interface CallableTransactionBlock<T> {
    T apply() throws Exception;
}
